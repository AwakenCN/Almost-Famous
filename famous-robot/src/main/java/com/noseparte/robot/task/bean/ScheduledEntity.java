package com.noseparte.robot.task.bean;

import lombok.Data;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 14:52
 * @Description:
 *
 *          <p>Robot 调度任务bean</p>
 */
@Data
public class ScheduledEntity {

    private Long robotId;
    private String key;
    private String group;
    private String cron;
    private Class jobClass;
    private State state;
    private Long tick;
    private Integer delaySecond;

    public ScheduledEntity(Long robotId, String key, String group, Class jobClass, String cron, Integer delaySecond) {
        this.robotId = robotId;
        this.key = key;
        this.group = group;
        this.jobClass = jobClass;
        this.cron = cron;
        this.delaySecond = delaySecond;
    }
}
