package com.liema.battle.battle;

import com.liema.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SLockStep extends Protocol {
    @Override
    protected void process() {
        log.debug("###################SLockStep####################");
        log.debug(toString());
    }
}
