package com.noseparte.battle.battle;

import LockstepProto.C2SLockStep;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CLockStep extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SLockStep req = C2SLockStep.parseFrom(msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }
        long roleId = getRoleId();
        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        BattleRoom battleRoom = battleMgr.getBattleRoomByRoleId(roleId);
        if (null != battleRoom) {
            log.error("战斗房间已经不存在,sid={}, roleId={}", getSid(), getRoleId());
            battleRoom.addFrame(req.getC().toByteArray());
        }
    }
}
