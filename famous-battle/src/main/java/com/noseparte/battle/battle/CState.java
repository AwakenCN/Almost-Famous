package com.noseparte.battle.battle;

import LockstepProto.C2SState;
import LockstepProto.State;
import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.asynchttp.BattleStartRequest;
import com.noseparte.battle.utils.JobEntity;
import com.noseparte.battle.utils.JobUtil;
import com.noseparte.common.battle.SimpleBattleRoom;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CState extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SState req = C2SState.parseFrom(this.msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }

        int state = req.getState();
        long roleId = req.getRoleId();
        long roomId = req.getRoomId();

        Session session = new Session();
        session.setRid(roleId);
        session.setChannel(this.getChannel());
        session.setSid(Misc.getSid(channel));
        LinkMgr.addSession(session);

        if (log.isDebugEnabled()) {
            log.debug("准备完毕roleId={}, roomId={}, state={}", roleId, roomId, state);
        }

        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        // 验证是否有本地战斗房间
        BattleRoom battleRoom = battleMgr.getBattleRoomById(roomId);
        if (null != battleRoom) {
            if (battleRoom.getState() == State.BATTLE_READY_GO_VALUE) {
                log.error("不能准备开始，玩家有未结束的战斗房间。role={}", roleId);
                return;
            }
        }
        if (null == battleRoom) {
            RedissonClient redissonClient = SpringContextUtils.getBean("redisson", RedissonClient.class);
            SimpleBattleRoom simpleBattleRoom = RedissonUtils.get(redissonClient, KeyPrefix.BattleRedisPrefix.BATTLE_ROOM + roomId, SimpleBattleRoom.class);
            battleMgr.addBattleRoom(simpleBattleRoom);
            battleRoom = battleMgr.getBattleRoomById(roomId);
            if (null == battleRoom) {
                log.error("严重异常，异常房间id={}", roomId);
                return;
            }
        }

        List<Actor> actors = battleRoom.getActors();
        int ready_go = 0;
        for (Actor actor : actors) {
            if (roleId == actor.getRid()) {
                if (state > 0) {
                    actor.setState(1);
                }
            }
            ready_go += actor.getState();
        }
        if (log.isDebugEnabled()) {
            log.debug("对战开始前准备人数与状态{}", ready_go);
        }
        // 准备状态相加数和匹配人数相等代表都准备好了
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        if (ready_go != battleServerConfig.getMatcher()) {
            return;
        }
        roomId = battleRoom.getRoomId();
        battleRoom.setState(State.BATTLE_READY_GO_VALUE);
        // 战斗开始
        JobEntity quartzEntity = new JobEntity();
        quartzEntity.setQuartzId(roomId);
        quartzEntity.setJobName("BattleRoomTask:" + roomId);
        quartzEntity.setJobGroup("WatchBattleRoomJob");
        quartzEntity.getInvokeParam().put("roomId", roomId);
        quartzEntity.getInvokeParam().put("startTime", System.currentTimeMillis());
        JobUtil jobUtil = SpringContextUtils.getBean("jobUtil", JobUtil.class);
        jobUtil.addJob(quartzEntity);
        // 保存定时器引用
        battleRoom.setQuartzEntity(quartzEntity);
        log.debug("开站了创建房间定时器===>>>>" + quartzEntity.getJobName());

        //向gamecore服务器发送战斗开始
        List<Long> rids = new ArrayList<>();
        for (Actor actor : actors) {
            rids.add(actor.getRid());
        }
        new BattleStartRequest(rids, roomId).execute();
    }
}
