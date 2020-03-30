package com.noseparte.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.resources.GlobalVariableConf;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.rpc.service.UniqueNameEnum;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.chapter.service.ChapterService;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.pack.service.BagCardStrategyService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.school.service.SchoolService;
import com.noseparte.game.sign.service.SignRewardService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noseparte
 * @date 2019/8/20 17:50
 * @Description
 */
@Slf4j
@Component
public class CreateRoleAction extends Action {

    @Autowired
    RoleService roleService;
    @Autowired
    CardService cardPackageService;
    @Autowired
    SchoolService schoolService;
    @Resource
    private RpcClient rpcClient;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ChapterService iChapterService;
    @Autowired
    private SignRewardService iSignRewardService;
    @Autowired
    MissionService iMissionService;
    @Autowired
    BagCardStrategyService iBagCardStrategyService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long uid = jsonObject.getLong("uid");
        // 模板中的数据
        // 模板中的数据
        Role role = roleService.selectById(uid);
        if (null == role) {
            String name = Misc.generatorName();
            UniqueNameEnum uniqueNameEnum = rpcClient.uniqueName(name);
            if (uniqueNameEnum != UniqueNameEnum.SUCCESS) {
                return Resoult.error(RegisterProtocol.CREATE_ROLE_ACTION_RESP, ErrorCode.REPEAT_NAME_ERROR, "Repeat name");
            }
            // get role id
            long rid = rpcClient.getUniqueId();
            // check login
            RBucket<String> bucket = redissonClient.getBucket(KeyPrefix.AdminRedisPrefix.ADMIN_USER_ID + uid);
            String token = bucket.get();
            if (token == null) {
                return Resoult.error(RegisterProtocol.CREATE_ROLE_ACTION_RESP, ErrorCode.TOKEN_EXPIRE_ERROR, "Token expire");
            }
            Role r = new Role();
            r.setRoleName(name);
            r.setUid(uid);
            r.setRid(rid);
            // 初始金币
            GlobalVariableConf globalVariable = ConfigManager.globalVariableConfMap.get(1018);
            r.setGold(Long.parseLong(globalVariable.getValue()));
            // 初始银币
            globalVariable = ConfigManager.globalVariableConfMap.get(1019);
            r.setSilver(Long.parseLong(globalVariable.getValue()));
            // 初始钻石
            globalVariable = ConfigManager.globalVariableConfMap.get(1020);
            r.setDiamond(Long.parseLong(globalVariable.getValue()));
            // 初始携带职业
            globalVariable = ConfigManager.globalVariableConfMap.get(1017);
            r.setSchool(Misc.cutQuotes(globalVariable.getValue()));
            String[] schools = Misc.split(globalVariable.getValue(), "\\,");
            if (null != schools && schools.length > 0) {
                for (String school : schools) {
                    int schoolId = Integer.parseInt(school);
                    this.schoolService.addSchoolByRoleId(rid, schoolId);
                }
            }
            // 初始最后战斗职业、最后战斗卡组、新手指引步骤
            r.setLastBattleSchool(0);
            r.setLastBattleCardGroup(0L);
            r.setNewcomerGuide(0);
            // 初始段位
            globalVariable = ConfigManager.globalVariableConfMap.get(1033);
            String[] battleRank = Misc.split(globalVariable.getValue(), "\\,");
            BattleRankBean battleRankBean = new BattleRankBean();
            battleRankBean.setRankId(Integer.parseInt(battleRank[0]));
            battleRankBean.setStarCount(Integer.parseInt(battleRank[1]));
            r.setBattleRank(battleRankBean);
            // save
            roleService.addRole(r);
            // 初始化卡包
            cardPackageService.initCard(rid);
            // 初始化签到奖励
            iSignRewardService.initSignRewardMgr(rid);
            // 初始化任务资源
            iMissionService.initMission(rid);
            // 初始化主线章节
            iChapterService.initChapter(rid);
            // 初始化背包(卡包)
            iBagCardStrategyService.initActorBag(rid);
            // 初始化战报
//            battleHistoryService.initBattleHistory(rid);

            return Resoult.ok(RegisterProtocol.CREATE_ROLE_ACTION_RESP);
        }
        return Resoult.error(RegisterProtocol.CREATE_ROLE_ACTION_RESP, ErrorCode.ROLE_EXIST, "");
    }
}
