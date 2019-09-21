package com.liema.battle.battle;

import LockstepProto.C2SPullFrame;
import LockstepProto.Frame;
import LockstepProto.NetMessage;
import LockstepProto.S2CPullFrame;
import com.liema.battle.server.LinkMgr;
import com.liema.battle.server.Protocol;
import com.liema.battle.server.Session;
import com.liema.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CPullFrame extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SPullFrame req = C2SPullFrame.parseFrom(msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }
        int src = req.getFrameIdBegin();
        int dest = req.getFrameIdEnd();
        Session session = getSession();
        long roleId = session.getRoleId();
        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        BattleRoom battleRoom = battleMgr.getBattleRoomByRoleId(roleId);
        //get range frame
        List<Frame.Builder> frames = battleRoom.pullRangeStoreFrame(src, dest);
        S2CPullFrame.Builder resp = S2CPullFrame.newBuilder();
        for (Frame.Builder frame : frames) {
            resp.addF(frame);
        }
        byte[] msg = resp.build().toByteArray();

        Protocol p = new SPullFrame();
        p.setType(NetMessage.S2C_PullFrame_VALUE);
        p.setMsg(msg);

        LinkMgr.send(getSid(), p);
    }
}
