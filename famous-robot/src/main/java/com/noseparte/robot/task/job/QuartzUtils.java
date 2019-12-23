package com.noseparte.robot.task.job;

import com.noseparte.robot.task.bean.ScheduledEntity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/25 15:27
 * @Description: <p>quartz 调度任务工具类</p>
 */
@Slf4j
@Service
public class QuartzUtils {

    @Resource
    @Qualifier("scheduler")
    private Scheduler scheduler;

    /**
     * 添加定时任务
     */
    public void addJob(ScheduledEntity scheduled) {
        try {
            //任务名，任务组，任务类
//            Object bean = SpringContextUtils.getBean(scheduled.getGroup(), scheduled.getJobClass());
            JobDetail jobDetail = JobBuilder.newJob(scheduled.getJobClass())
                    .withIdentity(scheduled.getKey(), scheduled.getGroup())
                    .build();

            Date startTime = new Date(System.currentTimeMillis()+scheduled.getDelaySecond()*1000);
            //触发器名，触发器组
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduled.getKey(), scheduled.getGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduled.getCron()))
//                    .startNow()
                    .startAt(startTime)
                    .build();
            trigger.getJobDataMap().put("robotId", scheduled.getRobotId());

            scheduler.scheduleJob(jobDetail, trigger);

            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        } finally {
            log.error("......添加定时任务......");
        }
    }

    /**
     * 修改一个任务的时间的触发时间
     *
     * @param jobName        任务名
     * @param jobGroup       任务组
     * @param triggerName    触发器名
     * @param triggerGroup   触发器组
     * @param cronExpression 触发时间表达式
     */
    public void modifyJob(String jobName, String jobGroup, String triggerName, String triggerGroup, String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                return;
            }
            //获取当前触发器的时间表达式
            String oldCronExpression = oldTrigger.getCronExpression();
            if (!oldCronExpression.equalsIgnoreCase(cronExpression)) {
                //触发器名，触发器组
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup);
                //设置任务触发的时间表达式
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
                triggerBuilder.startNow();
                //创建Trigger对象
                CronTrigger newTrigger = (CronTrigger) triggerBuilder.build();
                scheduler.rescheduleJob(triggerKey, newTrigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        } finally {
            log.error("......修改定时任务......");
        }
    }

    /**
     * 启动所有任务
     */
    public void startJob() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } finally {
            log.error("......启动所有定时任务......");
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void removeJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            //移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroup));
            //删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));

        } catch (SchedulerException e) {
            e.printStackTrace();
        } finally {
            log.error("......移除一个定时任务......");
        }
    }
}
