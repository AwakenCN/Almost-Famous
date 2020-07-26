package com.noseparte.robot.task.handler;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.GMScene;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.bag.BagBuyCmd;
import com.noseparte.robot.bag.BagBuyRequest;
import com.noseparte.robot.enitty.Role;
import com.noseparte.robot.gm.GameMaster;
import com.noseparte.robot.gm.GameMasterCmd;
import com.noseparte.robot.gm.GameMasterRequest;
import com.noseparte.robot.role.RoleListCmd;
import com.noseparte.robot.role.RoleListRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/22 10:02
 * @Description:
 *
 *          <p>买 卡包</p>
 *          <p>调度任务</p>
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BuyCardBagJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long robotId = context.getTrigger().getJobDataMap().getLong("robotId");
        RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
        Robot robot = robotMgr.getRobot(robotId);

        // 如果余额不足, 使用GM加钱 return。
        Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
        Integer price = itemConfMap.get(1001).getPrice();
        if(robot.getDiamond() < price*10){
            GameMasterCmd gameMasterCmd = new GameMasterCmd();
            gameMasterCmd.setCmd(RegisterProtocol.GAME_MASTER_REQ);
            gameMasterCmd.setUid(robot.getUid());
            gameMasterCmd.setRid(robotId);
            gameMasterCmd.setType(GMScene.money);
            String GameParams = GameMaster.generateGameParams(String.valueOf(AttrCode.DIAMOND.value()), String.valueOf(price*10),"0");
            gameMasterCmd.setGameParams(GameParams);
            try {
                JSONObject object = new GameMasterRequest(gameMasterCmd).callback();
                Integer code = object.getInteger("code");
                log.debug("robot {}， 少年你的钻石已到账", robotId);
                if(code == ErrorCode.SERVER_SUCCESS.value()){
                    RoleListCmd roleListCmd = new RoleListCmd();
                    roleListCmd.setCmd(RegisterProtocol.ROLE_LIST_ACTION_REQ);
                    roleListCmd.setRid(robotId);
                    roleListCmd.setUid(robot.getUid());
                    JSONObject jsonObject = new RoleListRequest(roleListCmd).callback();
                    Integer codeRole = jsonObject.getInteger("code");
                    String dataRole = jsonObject.getString("data");
                    if(codeRole == ErrorCode.SERVER_SUCCESS.value()){
                        // 修改robot 内存中的货币值
                        Role role = FastJsonUtils.toBean(dataRole, Role.class);
                        robot.setGold(role.getGold());
                        robot.setSilver(role.getSilver());
                        robot.setDiamond(role.getDiamond());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemId", 1001);
        jsonObject.put("code", 7);
        jsonObject.put("num", 10);
        jsonObjects.add(jsonObject);

        BagBuyCmd bagBuyCmd = new BagBuyCmd();
        bagBuyCmd.setRid(robot.getRid());
        bagBuyCmd.setPackages(jsonObjects.toString());
        bagBuyCmd.setCmd(RegisterProtocol.CARD_BAG_BUY_ACTION_REQ);
        try {
            if(log.isDebugEnabled()){
                log.debug("robot {}, 购买卡包成功", robotId);
            }
            new BagBuyRequest(bagBuyCmd).execute();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("robot {}， 抽卡包 >>>>>>>>>>>>> 失败", robotId);
            }
        }
    }
}
