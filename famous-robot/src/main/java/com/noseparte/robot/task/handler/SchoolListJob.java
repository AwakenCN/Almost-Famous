package com.noseparte.robot.task.handler;

import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.school.SchoolListCmd;
import com.noseparte.robot.school.SchoolListRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/15 15:01
 * @Description:
 *
 *          <p>robot 连续不间断地</p>
 *          <p>挑战关卡任务</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SchoolListJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        JobDataMap jobDataMap = trigger.getJobDataMap();

        long robotId = jobDataMap.getLong("robotId");
        // 职业
        SchoolListCmd schoolListCmd = new SchoolListCmd();
        schoolListCmd.setCmd(RegisterProtocol.SCHOOL_LIST_REQ);
        schoolListCmd.setRid(robotId);

        try {
            new SchoolListRequest(schoolListCmd).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
