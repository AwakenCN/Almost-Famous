package com.noseparte.match.match;

import LockstepProto.BattleActor;
import LockstepProto.NetMessage;
import LockstepProto.S2CMatch;
import com.noseparte.common.battle.*;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.match.FamousMatchApplication;
import com.noseparte.match.MatchServerConfig;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractMatch implements Runnable, ConnectCloseObserver {

    public static final int LeisureMode = 1;
    public static final int RankMode = 2;

    @Autowired
    protected RedissonClient redissonClient;

    @Autowired
    protected RpcClient rpcClient;

    @Autowired
    protected MatchServerConfig matchServerConfig;

    public abstract void add(Actor actor);

    public abstract void remove(Actor actor, MatchEnum reason);

    public abstract void start();

    public abstract void close();

    public boolean isHaveBattleRoom(long roleId) {
        RBucket<Long> roomId = redissonClient.getBucket(KeyPrefix.BattleRedisPrefix.BATTLE_ROOM_BY_ROLE + roleId);
        return roomId.get() == null ? false : true;
    }

    /**
     * @param roleId
     * @return 房间简单信息
     */
    public SimpleBattleRoom getSimpleBattleRoomByRoleId(long roleId) {
        RBucket<Long> roomId = redissonClient.getBucket(KeyPrefix.BattleRedisPrefix.BATTLE_ROOM_BY_ROLE + roleId);
        if (null == roomId)
            return null;
        RBucket<SimpleBattleRoom> room = redissonClient.getBucket(KeyPrefix.BattleRedisPrefix.BATTLE_ROOM + roomId.get());
        if (null == room)
            return null;
        return room.get();
    }

    /**
     * 拼装房间信息协议
     */
    public Protocol toS2CMatch(SimpleBattleRoom simpleBattleRoom) {
        S2CMatch.Builder s2CMatch = S2CMatch.newBuilder();
        for (Actor actor : simpleBattleRoom.getActors()) {
            BattleActor.Builder battleActor = BattleActor.newBuilder().setRoleId(actor.getRid()).addAllCardIds(actor.getUserCards())
                    .setAgi(actor.getAgi()).setIq(actor.getIq()).setStr(actor.getStr()).setRoleName(actor.getRoleName()).setSchool(actor.getSchoolId())
                    .setRankId(actor.getBattleRankBean().getRankId()).setStarCount(actor.getBattleRankBean().getStarCount()).setSchoolLv(actor.getSchoolLevel());
            s2CMatch.addActors(battleActor.build());
        }
        //
        byte[] respMsg = s2CMatch.setSeed(simpleBattleRoom.getSeed())
                .setMapId(simpleBattleRoom.getMapId())
                .setBattleStartTime(simpleBattleRoom.getCreateTime())
                .setHost(simpleBattleRoom.getHost())
                .setPort(simpleBattleRoom.getPort())
                .setRoomId(simpleBattleRoom.getRoomId())
                .setBattleMode(simpleBattleRoom.getBattleMode().getValue())
                .build().toByteArray();
        Protocol p = new SMatch();
        p.setType(NetMessage.S2C_Match_VALUE);
        p.setMsg(respMsg);

        return p;
    }

    protected void createSimpleBattleRoom(BattleModeEnum battleMode, Long... actorIds) {
        List<Actor> actors = new ArrayList<>(actorIds.length);
        for (Long actorId : actorIds) {
            String match_queue_by_role_key = KeyPrefix.BattleRedisPrefix.MATCH_QUEUE_BY_ROLE + actorId;
            RedissonUtils.lock(redissonClient, match_queue_by_role_key);
            Actor a = RedissonUtils.getAndDelete(redissonClient, match_queue_by_role_key, Actor.class);
            if (null != a)
                actors.add(a);
            RedissonUtils.unlock(redissonClient, match_queue_by_role_key);
        }

        SimpleBattleRoom battleRoom = new SimpleBattleRoom();
        battleRoom.setActors(actors);
        int seed = Misc.random(10000, 99999);
        battleRoom.setSeed(seed);
        int mapId = randomMap();
        battleRoom.setMapId(mapId);
        battleRoom.setCreateTime(System.currentTimeMillis());
        long roomId = rpcClient.getUniqueId();
        battleRoom.setRoomId(roomId);

        BattleService battleService = FamousMatchApplication.battleServices.first();
        battleRoom.setHost(battleService.getHost());
        battleRoom.setPort(battleService.getPort());
        battleRoom.setBattleMode(battleMode);

        // 设置房间
        String battle_room_key = KeyPrefix.BattleRedisPrefix.BATTLE_ROOM + roomId;
        int expireTime = matchServerConfig.getLifecycle() * 60;
        RedissonUtils.set(battleRoom, redissonClient, battle_room_key, expireTime);
        for (Long actorId : actorIds) {
            String battle_room_by_role_key = KeyPrefix.BattleRedisPrefix.BATTLE_ROOM_BY_ROLE + actorId;
            RedissonUtils.set(roomId, redissonClient, battle_room_by_role_key, expireTime);
        }
        // 发送给client
        Protocol p = toS2CMatch(battleRoom);
        // 返回客户端演员信息
        for (Long actorId : actorIds) {
            Session session = LinkMgr.getSession(actorId);
            LinkMgr.send(session, p);
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

    /**
     * 返回包含基数(baseNumber)前后几位(range)的数组或前或后(high or low)
     * @param baseNumber  基数
     * @param range       范围
     * @param isHigh       或是前或是后 high or low
     * @return    返回整数数组
     */
    public int[] rangeArr(int baseNumber, int range, boolean isHigh) {
        if (baseNumber <= 0 || range <= 0)
            return null;
        int[] rangArr = new int[range];
        // 小于基数的数字
        if (!isHigh) {
            for (int i = baseNumber, j = 0; i < range; i--, j++) {
                if (i == 0)
                    break;
                rangArr[j] = i;
            }
            return rangArr;
        }
        // 大于基数的数字
        for (int i = baseNumber, j = 0; i < range; i++, j++) {
            rangArr[j] = i;
        }
        return  rangArr;
    }

    public static int value(int baseNumber, int range, boolean minOrMax) {
        if (baseNumber <= 0 || range <= 0)
            return -1;
        if (!minOrMax) {
            for (int i = 0; i < range; i++) {
                if (baseNumber - 1 == 0)
                    break;
                baseNumber -= 1;
            }
            return baseNumber;
        }

        return baseNumber + range;
    }


    @Override
    public void update(int sid, MatchEnum matchEnum) {
        Session session = LinkMgr.getSession(sid);
        if (null == session) return;
        String match_queue_by_role_key = KeyPrefix.BattleRedisPrefix.MATCH_QUEUE_BY_ROLE + session.getRid();
        RBucket<Actor> bucket = redissonClient.getBucket(match_queue_by_role_key);
        Actor actor = bucket.get();
        if (null == actor) return;
        if (actor.getBattleMode() == BattleModeEnum.RANK) {
            RankMatchMgr rankMatchMgr = SpringContextUtils.getBean("rankMatchMgr", RankMatchMgr.class);
            rankMatchMgr.remove(actor, matchEnum);
        } else if (actor.getBattleMode() == BattleModeEnum.LEISURE) {
            LeisureMatchMgr leisureMatchMgr = SpringContextUtils.getBean("leisureMatchMgr", LeisureMatchMgr.class);
            leisureMatchMgr.remove(actor, matchEnum);
        }
        bucket.delete();
    }

}
