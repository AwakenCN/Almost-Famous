package com.noseparte.robot.task.handler;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.AttrCode;
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

import java.util.ArrayList;
import java.util.List;

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
public class SelectCardBagJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long robotId = context.getTrigger().getJobDataMap().getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Robot robot = robotMgr.getRobot(robotId);
        ActorBag actorBag = robot.getActorBag();
        List<JSONObject> jsonObjects = new ArrayList<>();
        BagSelectCmd bagSelectCmd;
        for(BagBean bag : actorBag.packages.values()){
            if(bag.getCode() == AttrCode.CARD_PACKAGE.value && bag.getNum() >= 10){ //十连抽
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId", bag.getItemId());
                jsonObject.put("code", bag.getCode());
                jsonObject.put("num", bag.getNum());
                jsonObjects.add(jsonObject);
                bagSelectCmd = new BagSelectCmd();
                bagSelectCmd.setRid(robot.getRid());
                bagSelectCmd.setPackages(jsonObjects.toString());
                bagSelectCmd.setCmd(RegisterProtocol.CARD_BAG_SELECT_ACTION_REQ);
                try {
                    if(log.isDebugEnabled()){
                        log.debug("robot {}, 抽卡包 {} 共计{}次", robotId, bag.getItemId(), bag.getNum());
                    }
                    new BagSelectRequest(bagSelectCmd).execute();
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("robot {}， 抽卡包 >>>>>>>>>>>>> 失败", robotId);
                    }
                }
            }
        }
    }
}
