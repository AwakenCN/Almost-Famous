package com.noseparte.robot.task.schedule;


/**
 * @Auther: Noseparte
 * @Date: 2019/9/26 10:09
 * @Description: 
 * 
 *          <p>robot 调度计划任务</p>
 */
public class CronExpression {

    public final static String WATCH_MISSION_LIST_CRON = "0/30 * * * * ?";
    public final static String CHALLENGE_CYCLE  = "0/20 0/1 * * * ? ";
    public final static String ACCEPT_SIGN_REWARD  = "0 0/5 * * * ? ";
    public final static String SELECT_CARD_BAG  = "0 0/1 * * * ? ";
    public final static String SCHOOL_LIST  = "0/20 * * * * ?";


}
