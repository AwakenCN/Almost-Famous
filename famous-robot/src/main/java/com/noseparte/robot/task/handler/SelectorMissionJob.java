package com.noseparte.robot.task.handler;

import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.mission.ActorListCmd;
import com.noseparte.robot.mission.ActorListRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/26 10:22
 * @Description:
 *
 *          <p>轮询在线机器人任务列表</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SelectorMissionJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long robotId = context.getTrigger().getJobDataMap().getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Robot robot = robotMgr.getRobot(robotId);
        ActorListCmd actorListCmd = new ActorListCmd();
        actorListCmd.setRid(robot.getRid());
        actorListCmd.setCmd(RegisterProtocol.MISSION_ACTOR_LIST_REQ);
        try {
            new ActorListRequest(actorListCmd).execute();
        } catch (Exception e) {
            log.error("轮询在线机器人[{}]任务列表失败", robot.getRid());
        }
    }
}
