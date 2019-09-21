package com.noseparte.battle.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
@AllArgsConstructor
public class WatchBattleRoomTriggerListener implements TriggerListener {

    String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        if (log.isDebugEnabled()) {
            log.debug("triggerFired = {}", getName());
        }
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        if (log.isDebugEnabled()) {
            log.debug("triggerComplete = {}", getName());
        }
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        if (log.isDebugEnabled()) {
            log.debug("triggerComplete = {}", getName());
        }
    }
}
