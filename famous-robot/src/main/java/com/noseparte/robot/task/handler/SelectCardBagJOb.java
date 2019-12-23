package com.noseparte.robot.task.handler;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.BagBean;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.bag.BagSelectCmd;
import com.noseparte.robot.bag.BagSelectRequest;
import com.noseparte.robot.enitty.ActorBag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/22 10:02
 * @Description:
 *
 *          <p>抽 卡包</p>
 *          <p>调度任务</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SelectCardBagJOb implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        long robotId = trigger.getJobDataMap().getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Robot robot = robotMgr.getRobot(robotId);
        ActorBag actorBag = robot.getActorBag();
        for(BagBean bag : actorBag.packages.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("itemId", bag.getItemId());
            jsonObject.put("code", bag.getCode());
            jsonObject.put("num", bag.getNum());
            BagSelectCmd bagSelectCmd = new BagSelectCmd();
            bagSelectCmd.setRid(robot.getRid());
            bagSelectCmd.setPackages(jsonObject.toJSONString());
            bagSelectCmd.setCmd(RegisterProtocol.CARD_BAG_SELECT_ACTION_REQ);
            try {
                new BagSelectRequest(bagSelectCmd).execute();
            } catch (Exception e) {
                if(log.isErrorEnabled()){
                    log.error("robot {}， 抽卡包 >>>>>>>>>>>>> 失败", robotId);
                }
            }
            break;
        }
    }
}
