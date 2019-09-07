package com.liema.battle.battle;

import com.liema.battle.BattleServerConfig;
import com.liema.battle.asynchttp.BattleEndRequest;
import com.liema.battle.server.LinkMgr;
import com.liema.battle.server.Protocol;
import com.liema.battle.server.Session;
import com.liema.common.bean.Actor;
import com.liema.common.bean.BattleActorResult;
import com.liema.common.bean.BattleRoomResult;
import com.liema.common.global.ConfigManager;
import com.liema.common.global.Misc;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BattleRoomMgr {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    @Autowired
    BattleServerConfig battleServerConfig;

    @Setter
    private volatile boolean stop = false;

    private static final int A_ACTOR = 0;
    private static final int B_ACTOR = 1;

    // 角色与战斗房间映射
    public static Map<Long, Long> roleRelationRoomMap = new ConcurrentHashMap<>();

    // 战斗房间
    public static Map<Long, BattleRoom> battleRoomMap = new ConcurrentHashMap<>();

    public BattleRoom createBattleRoom(List<Actor> actors) {
        BattleRoom battleRoom = new BattleRoom();
        battleRoom.setActors(actors);
        int seed = Misc.random(10000, 99999);
        battleRoom.setSeed(seed);
        int mapId = randomMap();
        battleRoom.setMapId(mapId);
        battleRoom.setCreateTime(System.currentTimeMillis());
        battleRoom.setState(-1);
        // TODO: RPC
        long roomId = Misc.random(10000, 300000);
        battleRoom.setRoomId(roomId);
        return battleRoom;
    }

    public void addBattleRoom(BattleRoom battleRoom) {
        Long roomId = battleRoom.getRoomId();
        battleRoomMap.put(roomId, battleRoom);
        for (Actor actor : battleRoom.getActors()) {
            roleRelationRoomMap.put(actor.getRoleId(), roomId);
        }
    }

    public BattleRoom getBattleRoomById(Long roomId) {
        return battleRoomMap.get(roomId);
    }

    public BattleRoom getBattleRoomByRoleId(long roleId) {
        long roomId = roleRelationRoomMap.get(roleId);
        return battleRoomMap.get(roomId);
    }

    public boolean isHaveRoom(Long roleId) {
        return roleRelationRoomMap.containsKey(roleId);
    }

    public void disbandBattleRoom(Long roomId) {
        BattleRoom battleRoom = battleRoomMap.remove(roomId);
        for (Actor actor : battleRoom.getActors()) {
            roleRelationRoomMap.remove(actor.getRoleId());
        }
    }

    public int randomMap() {
        int mapId = 0;
        int size = ConfigManager.mapConfMap.size();
        if (size > 0) {
            int r = Misc.random(0, size);
            Set<Integer> mapIdSet = ConfigManager.mapConfMap.keySet();
            int i = 0;
            for (Integer tmp : mapIdSet) {
                if (i == r) {
                    mapId = tmp;
                    break;
                }
                i++;
            }
        }
        return mapId;
    }

    public void roomOver(BattleRoom battleRoom) {
        try {
            disbandBattleRoom(battleRoom.getRoomId());
            // 通知game core
            new BattleEndRequest(battleRoom.getRoomId(), battleRoom.getBattleRoomResult().getWinners(),
                    battleRoom.getBattleRoomResult().getLosers()).execute();

            // 通知客户端
            Protocol p = battleRoom.toS2CBattleEnd();
            for (Actor actor : battleRoom.getActors()) {
                long roleId = actor.getRoleId();
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
                        battleRoomResult.getWinners().add(actor.getRoleId());
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


}
