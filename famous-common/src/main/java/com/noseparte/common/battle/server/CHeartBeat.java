package com.noseparte.common.battle.server;

import LockstepProto.C2SHeartBeat;
import LockstepProto.NetMessage;
import LockstepProto.S2CHeartBeat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CHeartBeat extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SHeartBeat req = C2SHeartBeat.parseFrom(msg);
        byte[] resmsg = S2CHeartBeat.newBuilder().setSeq(req.getSeq()).build().toByteArray();
        Protocol p = new CHeartBeat();
        p.setType(NetMessage.S2C_HeartBeat_VALUE);
        p.setMsg(resmsg);

        LinkMgr.send(channel, p);
    }
}
