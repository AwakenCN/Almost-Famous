package com.noseparte.battle.battle;

import LockstepProto.C2SState;
import LockstepProto.State;
import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.asynchttp.BattleStartRequest;
import com.noseparte.battle.server.Protocol;
import com.noseparte.battle.utils.JobEntity;
import com.noseparte.battle.utils.JobUtil;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

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
        long roleId = getRoleId();
        if (log.isDebugEnabled()) {
            log.debug("准备完毕roleId={}, state={}", roleId, state);
        }

        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        // 验证是否有存在的房间
        if (!battleMgr.isHaveRoom(roleId)) {
            log.error("不能准备开始，玩家有未结束的战斗房间。role={}", roleId);
            return;
        }

        BattleRoom battleRoom = battleMgr.getBattleRoomByRoleId(roleId);
        List<Actor> actors = battleRoom.getActors();
        int ready_go = 0;
        for (Actor actor : actors) {
            if (roleId == actor.getRoleId()) {
                actor.setState(state);
            }
            ready_go += actor.getState();
        }
        if (log.isDebugEnabled()) {
            log.debug("对战开始前准备人数与状态{}", ready_go);
        }
        // 准备状态相加数和匹配人数相等代表都准备好了
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        if (ready_go == battleServerConfig.getMatchers()) {
            Long roomId = battleRoom.getRoomId();
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
                rids.add(actor.getRoleId());
            }
            new BattleStartRequest(rids, roomId).execute();
        }
    }
}
