package com.noseparte.robot.task.handler;

import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.sign.SignInCmd;
import com.noseparte.robot.sign.SignInRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Map;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/17 10:26
 * @Description:
 *
 *          <p>领取签到奖励</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AcceptSignRewardJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        long robotId = jobDataMap.getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Robot robot = robotMgr.getRobot(robotId);
        Map<Integer, RewardBean> rewards = robot.getSignReward().getRewards();
        for (RewardBean reward : rewards.values()) {
            if (reward.getStatus() == StateCode.IN_PROGRESS.value()) {
                // 签到
                SignInCmd signInCmd = new SignInCmd();
                signInCmd.setDay(reward.getDay());
                signInCmd.setRid(robot.getRid());
                signInCmd.setCmd(RegisterProtocol.SIGN_IN_ACTION_REQ);
                try {
                    new SignInRequest(signInCmd).execute();
                } catch (Exception e) {
                    log.error("rid, {}, 签到奖励{{}}领取失败", robot.getRid(), reward.getDay());
                }
                break;
            }
        }
    }
}
