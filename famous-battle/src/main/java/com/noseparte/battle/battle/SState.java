package com.noseparte.battle.battle;

import com.noseparte.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SState extends Protocol {
    @Override
    protected void process() {
        log.debug("###################SState####################");
        log.debug(toString());
    }
}
