package com.noseparte.battle.battle;

import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.asynchttp.BattleEndRequest;
import com.noseparte.common.battle.SimpleBattleRoom;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.bean.BattleActorResult;
import com.noseparte.common.bean.BattleRoomResult;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.rpc.RpcClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BattleRoomMgr {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    @Autowired
    BattleServerConfig battleServerConfig;
    @Resource
    RpcClient rpcClient;
    @Autowired
    private RedissonClient redissonClient;

    @Setter
    private volatile boolean stop = false;

    private static final int A_ACTOR = 0;
    private static final int B_ACTOR = 1;

    // 角色与战斗房间映射
    private Map<Long, Long> roleRelationRoomMap = new ConcurrentHashMap<>();
    // 战斗房间
    private Map<Long, BattleRoom> battleRoomMap = new ConcurrentHashMap<>();

    public void addBattleRoom(SimpleBattleRoom simpleBattleRoom) {
        BattleRoom battleRoom = new BattleRoom();
        battleRoom.setRoomId(simpleBattleRoom.getRoomId());
        battleRoom.setMapId(simpleBattleRoom.getMapId());
        battleRoom.setSeed(simpleBattleRoom.getSeed());
        battleRoom.setActors(simpleBattleRoom.getActors());
        battleRoom.setCreateTime(simpleBattleRoom.getCreateTime());
        battleRoom.setBattleMode(simpleBattleRoom.getBattleMode());

        Long roomId = battleRoom.getRoomId();
        battleRoomMap.put(roomId, battleRoom);
        for (Actor actor : battleRoom.getActors()) {
            roleRelationRoomMap.put(actor.getRid(), roomId);
        }
    }

    public BattleRoom getBattleRoomById(Long roomId) {
        return battleRoomMap.get(roomId);
    }

    public BattleRoom getBattleRoomByRoleId(long roleId) {
        long roomId = roleRelationRoomMap.get(roleId);
        return battleRoomMap.get(roomId);
    }

    public boolean isLocalHaveBattleRoom(Long roleId) {
        return roleRelationRoomMap.containsKey(roleId);
    }

    public void disbandBattleRoom(Long roomId) {
        BattleRoom battleRoom = battleRoomMap.remove(roomId);
        String battle_room_key = KeyPrefix.BattleRedisPrefix.BATTLE_ROOM + roomId;
        RedissonUtils.lock(redissonClient, battle_room_key);
        RedissonUtils.delete(redissonClient, battle_room_key);
        RedissonUtils.unlock(redissonClient, battle_room_key);


        for (Actor actor : battleRoom.getActors()) {
            roleRelationRoomMap.remove(actor.getRid());

            long roleId = actor.getRid();
            String battle_room_by_role_key = KeyPrefix.BattleRedisPrefix.BATTLE_ROOM_BY_ROLE + roleId;
            RedissonUtils.lock(redissonClient, battle_room_by_role_key);
            RedissonUtils.delete(redissonClient, battle_room_by_role_key);
            RedissonUtils.unlock(redissonClient, battle_room_by_role_key);
        }
    }

    public void roomOver(BattleRoom battleRoom) {
        try {
            disbandBattleRoom(battleRoom.getRoomId());
            // 通知game core
            new BattleEndRequest(battleRoom.getRoomId(), battleRoom.getBattleRoomResult().getWinners(),
                    battleRoom.getBattleRoomResult().getLosers(), battleRoom.getBattleMode()).execute();

            // 通知客户端
            Protocol p = battleRoom.toS2CBattleEnd();
            for (Actor actor : battleRoom.getActors()) {
                long roleId = actor.getRid();
                Session session = LinkMgr.getSession(roleId);
                if (null == session)
                    continue;
                LinkMgr.send(session.getSid(), p);
            }
        } catch (Exception e) {
            log.error("战斗结束回调gamecore：", e);
        }
    }

    public boolean checkBattleResult(BattleRoom battleRoom, long checkPoint) {
        BattleRoomResult battleRoomResult = battleRoom.getBattleRoomResult();
        if (battleRoom.battleActorResults.size() == 1) {
            // 3秒后还未收到B的战斗结果，以A的战斗结果为准
            BattleActorResult battleActorResultA = battleRoom.battleActorResults.get(A_ACTOR);
            if (checkPoint - battleActorResultA.getUploadTime() > battleServerConfig.getOtherActorBattleResult()) {
                if (log.isInfoEnabled()) {
                    log.info("(1)---3秒后还未收到B的战斗结果,以A的战斗结果为准.");
                }
                battleRoomResult.getWinners().addAll(battleActorResultA.getBattleReport().getWinners());
                battleRoomResult.getLosers().addAll(battleActorResultA.getBattleReport().getLosers());
                return false;
            }
        } else if (battleRoom.battleActorResults.size() == 2) {
            // 三秒内收到B的战斗结果
            BattleActorResult battleActorResultA = battleRoom.battleActorResults.get(A_ACTOR);
            BattleActorResult battleActorResultB = battleRoom.battleActorResults.get(B_ACTOR);
            if (checkPoint - battleActorResultA.getUploadTime() <= battleServerConfig.getOtherActorBattleResult()) {
                // A和B结果一致
                if (Misc.isListEqual(battleActorResultA.getBattleReport().getWinners(), battleActorResultB.getBattleReport().getWinners())
                        && Misc.isListEqual(battleActorResultA.getBattleReport().getLosers(), battleActorResultB.getBattleReport().getLosers())) {
                    if (log.isInfoEnabled()) {
                        log.info("(2)---三秒内收到,A和B结果一致.");
                    }
                    battleRoomResult.getWinners().addAll(battleActorResultA.getBattleReport().getWinners());
                    battleRoomResult.getLosers().addAll(battleActorResultA.getBattleReport().getLosers());
                    return false;
                }
                // 有异常A和B都获胜(非正常情况要投递到验证服务器)
                else if (battleActorResultA.getOverState() == BattleActorResult.EXCEPTION
                        || battleActorResultB.getOverState() == BattleActorResult.EXCEPTION) {
                    if (log.isInfoEnabled()) {
                        log.info("(3)---三秒内收到,有异常A和B都获胜(非正常情况要投递到验证服务器).");
                    }
                    for (Actor actor : battleRoom.getActors()) {
                        battleRoomResult.getWinners().add(actor.getRid());
                    }
                    return false;
                }
                // A和B战斗结果不一致(非正常情况要投递到验证服务器)
                else if (!Misc.isListEqual(battleActorResultA.getBattleReport().getWinners(), battleActorResultB.getBattleReport().getWinners())
                        || !Misc.isListEqual(battleActorResultA.getBattleReport().getLosers(), battleActorResultB.getBattleReport().getLosers())) {
                    if (log.isInfoEnabled()) {
                        log.info("(4)---三秒内收到,A和B战斗结果不一致(非正常情况要投递到验证服务器).");
                    }
                    // 这个情况要投递到验证服务器
                    return false;
                }
            }
        }
        return true;
    }

    public int getBattleRoomCount() {
        return this.battleRoomMap.size();
    }


}
