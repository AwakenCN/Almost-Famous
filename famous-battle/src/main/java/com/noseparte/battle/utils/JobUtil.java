package com.noseparte.battle.utils;

import com.noseparte.battle.BattleServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class JobUtil {

    @Resource
    @Qualifier("scheduler")
    private Scheduler scheduler;

    @Autowired
    private BattleServerConfig battleServerConfig;

    /**
     * 新建一个任务
     */
    public void addJob(JobEntity quartzEntity) throws Exception {

        JobDetail jobDetail = null;
        //构建job信息
        if ("WatchBattleRoomJob".equals(quartzEntity.getJobGroup())) {
            jobDetail = JobBuilder.newJob(WatchBattleRoomJob.class)
                    .withIdentity(quartzEntity.getJobName(), quartzEntity.getJobGroup())
                    .build();
        }/* else if ("BattleRoomBroadcastJob".equals(quartzEntity.getJobGroup())) {
            jobDetail = JobBuilder.newJob(WatchBattleRoomJob.class).withIdentity(quartzEntity.getJobName(), quartzEntity.getJobGroup()).build();
        }*/

        Trigger trigger = null;
        int intervalInMilliseconds = 1000 / battleServerConfig.getFrameSpeed();
        int repeatCount = battleServerConfig.getLifecycle() * 60 * 1000 / intervalInMilliseconds;

        trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzEntity.getJobName(), quartzEntity.getJobGroup())
                .startNow()
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInMilliseconds(intervalInMilliseconds)
//                        .withRepeatCount(1)
//                        .withMisfireHandlingInstructionIgnoreMisfires())
                .build();

        //传递参数
        if (quartzEntity.getInvokeParam() != null && quartzEntity.getInvokeParam().size() > 0) {
            Set<Map.Entry<String, Object>> entries = quartzEntity.getInvokeParam().entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                trigger.getJobDataMap().put(entry.getKey(), entry.getValue());
            }
        }
        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.getListenerManager().addTriggerListener(new WatchBattleRoomTriggerListener(quartzEntity.getJobName()));
//        scheduler.getListenerManager().addJobListener(new WatchBattleRoomJobListener(quartzEntity.getJobName()));
    }

    /**
     * 获取Job状态
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public String getJobState(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        return scheduler.getTriggerState(triggerKey).name();
    }

    //暂停所有任务
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    //暂停任务
    public String pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
//            if (log.isDebugEnabled()) {
//                log.debug("pauseJob = {}, {}, fail.", jobName, jobGroup);
//            }
            return "fail";
        } else {
            scheduler.pauseJob(jobKey);
//            if (log.isDebugEnabled()) {
//                log.debug("pauseJob = {}, {}, success.", jobName, jobGroup);
//            }
            return "success";
        }

    }

    //恢复所有任务
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    // 恢复某个任务
    public String resumeJob(String jobName, String jobGroup) throws SchedulerException {

        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
//            if (log.isDebugEnabled()) {
//                log.debug("resumeJob = {}, {}, fail.", jobName, jobGroup);
//            }
            return "fail";
        } else {
            scheduler.resumeJob(jobKey);
//            if (log.isDebugEnabled()) {
//                log.debug("resumeJob = {}, {}, success.", jobName, jobGroup);
//            }
            return "success";
        }
    }

    //删除某个任务
    public String deleteJob(JobEntity appQuartz) throws SchedulerException {
        JobKey jobKey = new JobKey(appQuartz.getJobName(), appQuartz.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return "jobDetail is null";
        } else if (!scheduler.checkExists(jobKey)) {
            return "jobKey is not exists";
        } else {
            scheduler.deleteJob(jobKey);
            return "success";
        }

    }

    //修改任务
    public String modifyJob(JobEntity appQuartz) throws SchedulerException {
        if (!CronExpression.isValidExpression(appQuartz.getCronExpression())) {
            return "Illegal cron expression";
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(appQuartz.getJobName(), appQuartz.getJobGroup());
        JobKey jobKey = new JobKey(appQuartz.getJobName(), appQuartz.getJobGroup());
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //表达式调度构建器,不立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(appQuartz.getCronExpression()).withMisfireHandlingInstructionDoNothing();
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            //修改参数
            if (!trigger.getJobDataMap().get("invokeParam").equals(appQuartz.getInvokeParam())) {
                trigger.getJobDataMap().put("invokeParam", appQuartz.getInvokeParam());
            }
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            return "success";
        } else {
            return "job or trigger not exists";
        }

    }
}

