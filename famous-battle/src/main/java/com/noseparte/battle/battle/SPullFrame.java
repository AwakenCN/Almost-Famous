package com.noseparte.battle.battle;

import com.noseparte.common.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SPullFrame extends Protocol {
    @Override
    protected void process() throws Exception {
        log.debug("###################SPullFrame####################");
        log.debug(toString());
    }
}
