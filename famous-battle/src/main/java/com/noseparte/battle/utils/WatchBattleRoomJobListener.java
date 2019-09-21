package com.noseparte.battle.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
@AllArgsConstructor
public class WatchBattleRoomJobListener implements JobListener {

    String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        if (log.isDebugEnabled()) {
            log.debug("jobToBeExecuted = {}", getName());
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        if (log.isDebugEnabled()) {
            log.debug("jobExecutionVetoed = {}", getName());
        }
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (log.isDebugEnabled()) {
            log.debug("jobWasExecuted = {}", getName());
        }
    }
}
