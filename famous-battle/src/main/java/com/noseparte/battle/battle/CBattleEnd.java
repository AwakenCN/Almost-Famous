package com.noseparte.battle.battle;

import LockstepProto.C2SBattleEnd;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.bean.BattleActorResult;
import com.noseparte.common.bean.BattleReportBean;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CBattleEnd extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SBattleEnd req = C2SBattleEnd.parseFrom(msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }

        List<Long> winners = req.getWinnersList();
        List<Long> losers = req.getLosersList();
        int isException = req.getState();// 0=normal, 1=exception, 2=surrender

        long roleId = getRoleId();
        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        BattleRoom battleRoom = battleMgr.getBattleRoomByRoleId(roleId);

        // 收集战斗结果
        long now = System.currentTimeMillis();
        BattleReportBean battleReport = new BattleReportBean();
        battleReport.setWinners(winners);
        battleReport.setLosers(losers);
        BattleActorResult battleActorResult = new BattleActorResult(roleId, now, isException, battleReport);
        battleRoom.collectBattleResult(battleActorResult);


    }
}
