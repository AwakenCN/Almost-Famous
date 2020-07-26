package com.noseparte.robot;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.bag.BagListCmd;
import com.noseparte.robot.bag.BagListRequest;
import com.noseparte.robot.cardpackage.CardListCmd;
import com.noseparte.robot.cardpackage.CardListRequest;
import com.noseparte.robot.chapter.cmd.ProgressCmd;
import com.noseparte.robot.chapter.request.ProgressRequest;
import com.noseparte.robot.enitty.*;
import com.noseparte.robot.mission.ActorListCmd;
import com.noseparte.robot.mission.ActorListRequest;
import com.noseparte.robot.role.GetRoleAttrCmd;
import com.noseparte.robot.role.GetRoleAttrRequest;
import com.noseparte.robot.school.SchoolListCmd;
import com.noseparte.robot.school.SchoolListRequest;
import com.noseparte.robot.sign.RewardListCmd;
import com.noseparte.robot.sign.RewardListRequest;
import com.noseparte.robot.task.factory.RobotFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Robot implements Runnable {

    public long uid;
    public String token;
    public long rid;
    public String roleName;
    public String school;
    public Long gold;
    public Long silver;
    public Long diamond;
    public int index;
    /**
     * 段位
     */
    private BattleRankBean battleRank;
    public Mission mission;
    public School occupation;
    public Card cardPackage;
    public Chapter chapter;
    public ActorBag actorBag;
    public SignReward signReward;

    @Override
    public void run() {

        if (FamousRobotApplication.robotModel.equals(RobotType.CLUSTER)) {
            // 初始化 robot
            boolean initiative = initializeRobot(rid);
            if (initiative) {
                // 给robot 分发定时任务
                RobotFactory robotFactory = SpringContextUtils.getBean("robotFactory", RobotFactory.class);
                RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
                Robot robot = robotMgr.getRobot(rid);
                robotFactory.dispatch(robot);
            }
        } else if (FamousRobotApplication.robotModel.equals(RobotType.SINGE)) {

            switch (FamousRobotApplication.robotType) {
                case RobotType.ROLE:
                    GetRoleAttrCmd getRoleAttrCmd = new GetRoleAttrCmd();
                    getRoleAttrCmd.setCmd(RegisterProtocol.ROLE_ATTR_ACTION_REQ);
                    getRoleAttrCmd.setAttrId(AttrCode.GOLD.value);
                    getRoleAttrCmd.setRid(getRid());
                    try {
                        new GetRoleAttrRequest(getRoleAttrCmd).execute();
                    } catch (Exception e) {
                        log.error("Get role attr error: ", e);
                    }
                    break;
                case RobotType.SCHOOL:
                    SchoolListCmd schoolListCmd = new SchoolListCmd();
                    schoolListCmd.setCmd(RegisterProtocol.SCHOOL_LIST_REQ);
                    schoolListCmd.setRid(getRid());
                    try {
                        new SchoolListRequest(schoolListCmd).execute();
                    } catch (Exception e) {
                        log.error("获取玩家职业列表失败: ", e);
                    }
                    break;
                case RobotType.CARD_PACKAGE:
                    CardListCmd cardListCmd = new CardListCmd();
                    cardListCmd.setCmd(RegisterProtocol.CARD_LIST_ACTION_REQ);
                    cardListCmd.setRid(getRid());
                    try {
                        new CardListRequest(cardListCmd).execute();
                    } catch (Exception e) {
                        log.error("获取卡牌列表失败: ", e);
                    }
                    break;
                case RobotType.MISSION:
                    ActorListCmd actorListCmd = new ActorListCmd();
                    actorListCmd.setCmd(RegisterProtocol.MISSION_ACTOR_LIST_REQ);
                    actorListCmd.setRid(getRid());
                    try {
                        new ActorListRequest(actorListCmd).execute();
                    } catch (Exception e) {
                        log.error("获取任务列表失败: ", e);
                    }
                    break;
                case RobotType.CHAPTER:
                    ProgressCmd progressCmd = new ProgressCmd();
                    progressCmd.setCmd(RegisterProtocol.CHAPTER_PROGRESS_REQ);
                    progressCmd.setRid(getRid());
                    try {
                        new ProgressRequest(progressCmd).execute();
                    } catch (Exception e) {
                        log.error("获取玩家章节列表失败: ", e);
                    }
                    break;
                case RobotType.SIGN:
                    RewardListCmd rewardListCmd = new RewardListCmd();
                    rewardListCmd.setCmd(RegisterProtocol.SIGN_REWARD_LIST_REQ);
                    rewardListCmd.setRid(getRid());
                    try {
                        new RewardListRequest(rewardListCmd).execute();
                    } catch (Exception e) {
                        log.error("获取签到记录失败: ", e);
                    }
                    break;
                case RobotType.BAG:
                    BagListCmd bagListCmd = new BagListCmd();
                    bagListCmd.setCmd(RegisterProtocol.CARD_BAG_LIST_REQ);
                    bagListCmd.setRid(getRid());
                    try {
                        new BagListRequest(bagListCmd).execute();
                    } catch (Exception e) {
                        log.error("获取玩家背包列表失败: ", e);
                    }
                    break;
                default:
                    break;

            }
        }
    }

    private boolean initializeRobot(Long rid) {
        try {
            List<Boolean> isAllCompleted = new ArrayList<>();
            // 任务列表
            ActorListCmd actorListCmd = new ActorListCmd();
            actorListCmd.setCmd(RegisterProtocol.MISSION_ACTOR_LIST_REQ);
            actorListCmd.setRid(rid);
            new ActorListRequest(actorListCmd, isAllCompleted).execute();
            // 职业
            SchoolListCmd schoolListCmd = new SchoolListCmd();
            schoolListCmd.setCmd(RegisterProtocol.SCHOOL_LIST_REQ);
            schoolListCmd.setRid(rid);
            new SchoolListRequest(schoolListCmd, isAllCompleted).execute();
            if(log.isInfoEnabled()){
                log.info("初始化robot {} 的职业列表 ", rid);
            }
            // 卡包
            CardListCmd cardListCmd = new CardListCmd();
            cardListCmd.setCmd(RegisterProtocol.CARD_LIST_ACTION_REQ);
            cardListCmd.setRid(rid);
            new CardListRequest(cardListCmd, isAllCompleted).execute();
            // 关卡
            ProgressCmd progressCmd = new ProgressCmd();
            progressCmd.setCmd(RegisterProtocol.CHAPTER_PROGRESS_REQ);
            progressCmd.setRid(rid);
            new ProgressRequest(progressCmd, isAllCompleted).execute();
            // 背包
            BagListCmd bagListCmd = new BagListCmd();
            bagListCmd.setCmd(RegisterProtocol.CARD_BAG_LIST_REQ);
            bagListCmd.setRid(rid);
            new BagListRequest(bagListCmd, isAllCompleted).execute();
            // 签到
            RewardListCmd rewardListCmd = new RewardListCmd();
            rewardListCmd.setCmd(RegisterProtocol.SIGN_REWARD_LIST_REQ);
            rewardListCmd.setRid(rid);
            new RewardListRequest(rewardListCmd, isAllCompleted).execute();

            while (true) {
                int size = isAllCompleted.size();
                log.debug("等待初始化的机器人id={},当前完成数量={}", rid, size);
                if (size >= 7) {
                    log.debug("======>>完成初始化的机器人id={}", rid);
                    break;
                }
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("robot {} 初始化失败", rid);
            }
            return false;
        }
        return true;
    }



}
