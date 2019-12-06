package com.noseparte.match.match;

import LockstepProto.S2CMatch;
import com.noseparte.common.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SMatch extends Protocol {
    @Override
    protected void process() throws Exception {
        S2CMatch req = S2CMatch.parseFrom(this.msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }
    }
}
