package com.noseparte.robot.task.factory;

import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.task.bean.ScheduledEntity;
import com.noseparte.robot.task.handler.AcceptSignRewardJob;
import com.noseparte.robot.task.handler.ChallengeCycleJob;
import com.noseparte.robot.task.handler.SelectCardBagJOb;
import com.noseparte.robot.task.handler.SelectorMissionJob;
import com.noseparte.robot.task.job.QuartzUtils;
import com.noseparte.robot.task.schedule.CronExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/24 17:00
 * @Description: 
 * 
 *          <p>机器人调度中心</p>
 */
@Slf4j
@Component
public class RobotFactory {

    @Resource
    RobotMgr robotMgr;

    public void dispatch(Robot robot){
        if(!robotMgr.getRobotMap().containsValue(robot)){
            return;
        }
        long robotId = robot.getRid();
        QuartzUtils quartzUtils = SpringContextUtils.getBean("quartzUtils", QuartzUtils.class);
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "chapter:" + robotId,"challengeCycleJob", ChallengeCycleJob.class, CronExpression.CHALLENGE_CYCLE, 60)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "mission" + robotId,"selectorMissionJob", SelectorMissionJob.class, CronExpression.WATCH_MISSION_LIST_CRON, 60)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "sign" + robotId,"acceptSignRewardJob", AcceptSignRewardJob.class, CronExpression.ACCEPT_SIGN_REWARD, 60)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "bag" + robotId,"selectCardBagJOb", SelectCardBagJOb.class, CronExpression.SELECT_CARD_BAG, 60)
        );
//        quartzUtils.addJob(
//                new ScheduledEntity(robot.getRid(), "school" + robotId,"schoolListJob", SchoolListJob.class, CronExpression.SCHOOL_LIST, 20)
//        );
    }

}
