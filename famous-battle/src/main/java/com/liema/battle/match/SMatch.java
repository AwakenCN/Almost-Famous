package com.liema.battle.match;

import LockstepProto.C2SState;
import LockstepProto.NetMessage;
import com.liema.battle.battle.CState;
import com.liema.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMatch extends Protocol {
    @Override
    protected void process() {
        log.debug("###################SMatch####################");
        log.debug(toString());

        byte[] respMsg = C2SState.newBuilder().setState(1).build().toByteArray();
        Protocol p = new CState();
        p.setType(NetMessage.C2S_State_VALUE);
        p.setMsg(respMsg);
        if (channel.isActive()) {
            channel.writeAndFlush(p);
            log.debug("send C2SState 准备就绪");
        }
    }
}
