package com.liema.battle.battle;

import com.liema.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SPullFrame extends Protocol {
    @Override
    protected void process() throws Exception {
        log.debug("###################SPullFrame####################");
        log.debug(toString());
    }
}
