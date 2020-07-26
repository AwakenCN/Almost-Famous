package com.noseparte.robot.task.factory;

import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.task.bean.ScheduledEntity;
import com.noseparte.robot.task.handler.*;
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
        int delaySecond = robot.getIndex() - FamousRobotApplication.index;
        QuartzUtils quartzUtils = SpringContextUtils.getBean("quartzUtils", QuartzUtils.class);
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "chapter:" + robotId, "challengeCycleJob", ChallengeCycleJob.class, CronExpression.CHALLENGE_CYCLE, delaySecond)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "mission:" + robotId, "selectorMissionJob", SelectorMissionJob.class, CronExpression.WATCH_MISSION_LIST_CRON, delaySecond)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "sign:" + robotId, "acceptSignRewardJob", AcceptSignRewardJob.class, CronExpression.ACCEPT_SIGN_REWARD, delaySecond)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "bag_select:" + robotId, "selectCardBagJOb", SelectCardBagJob.class, CronExpression.SELECT_CARD_BAG, delaySecond)
        );
        quartzUtils.addJob(
                new ScheduledEntity(robot.getRid(), "bag_buy:" + robotId, "buyCardBagJob", BuyCardBagJob.class, CronExpression.BUY_CARD_BAG, delaySecond)
        );
//        quartzUtils.addJob(
//                new ScheduledEntity(robot.getRid(), "battle:" + robotId, "battleEndlessJob", BattleEndlessJob.class, CronExpression.BATTLE_ENDLESS, delaySecond)
//        );
    }

}
