package com.noseparte.battle.battle;

import com.noseparte.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SReconnect extends Protocol {
    @Override
    protected void process() throws Exception {
        log.debug("###################SReconnect####################");
        log.debug(toString());
    }
}
