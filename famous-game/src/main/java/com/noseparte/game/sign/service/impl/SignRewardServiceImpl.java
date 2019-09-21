package com.noseparte.game.sign.service.impl;

import com.alibaba.fastjson.JSON;
import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.common.utils.DateUtils;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.sign.entity.SignReward;
import com.noseparte.game.sign.mongo.SignRewardDao;
import com.noseparte.game.sign.service.SignRewardService;
import jodd.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SignRewardServiceImpl implements SignRewardService {

    @Resource
    private SignRewardDao signRewardDao;
    @Autowired
    private RoleService roleService;

    // 玩家登陆奖励
    private static Map<Long, Map<Integer, RewardBean>> actorRewards = new ConcurrentHashMap<>(7);
    // 开服时间  ConfigManager.globalVariableMap.get(1033).toString()
    private static Long OnLineTime = DateUtils.stringToDate("2019-07-06", DateUtils.DATE_PATTERN).getTime();

    public static synchronized Map<Integer, RewardBean> getInstance() {
        Map<Integer, RewardBean> signRewards = new ConcurrentHashMap<>(7);
        signRewards.putIfAbsent(1, daily(1, 1026));
        signRewards.putIfAbsent(2, daily(2, 1027));
        signRewards.putIfAbsent(3, daily(3, 1028));
        signRewards.putIfAbsent(4, daily(4, 1029));
        signRewards.putIfAbsent(5, daily(5, 1030));
        signRewards.putIfAbsent(6, daily(6, 1031));
        signRewards.putIfAbsent(7, daily(7, 1032));
        return signRewards;
    }

    @Override
    public void initSignRewardMgr(Long rid) {
        Map<Integer, RewardBean> actor = getInstance();
        actorRewards.putIfAbsent(rid, actor);
        SignReward sign = new SignReward();
        sign.setRid(rid);
        sign.setRewards(actor);
        signRewardDao.insertActorReward(sign);
    }

    @Override
    public SignReward getSignHistoryByRole(Long rid) {
        return signRewardDao.getSignHistoryByRole(rid);
    }

    @Override
    public void updateActorSignHistory(SignReward signReward) {
        signRewardDao.updateActorSignHistory(signReward);
    }

    @Override
    public ErrorCode getSignReward(Long rid, Integer day) {
        if (day <= 0 || day > 7) {
            return ErrorCode.CLIENT_PARAMS_ERROR;
        }
        Role actor = roleService.selectByRoleId(rid);
        if (Objects.isNull(actor)) {
            return ErrorCode.ACCOUNT_NOT_EXIST;
        }
        SignReward signReward = getSignHistoryByRole(rid);
        if (Objects.isNull(signReward)) {
            return ErrorCode.SIGN_ERROR;
        }
        Map<Integer, RewardBean> actorRewards = signReward.getRewards();
        RewardBean sign = actorRewards.values().stream()
                .filter(rewardBean -> rewardBean.getDay() == day).findFirst().get();
        // 判断领取时间
        if (sign.getTakeTick() < new Date().getTime() && sign.getStatus() != StateCode.COMPLETED.value()) {
            // 发放奖励
            Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
            String quotes = Misc.cutQuotes(sign.getDrop());
            String[] drops = Misc.split(quotes, "\\,");
            ItemConf itemConf = itemConfMap.get(Integer.parseInt(drops[0]));
            List<HoldItem> holdItems = ItemMgr.dropItem(itemConf.getDrop());
            boolean issued = roleService.distributeAwardToActor(actor, holdItems);
            if (issued) {
                log.info("发放奖励成功： role={}, 奖励内容: holdItems={}, 发放时间： distributeTick={}",
                        actor.getRid(), JSON.toJSONString(holdItems), new Date().getTime());
            }
            sign.setStatus(StateCode.COMPLETED.value());
        } else {
            return ErrorCode.REWARD_UNAVAILABLE_TIME;
        }
        actorRewards.putIfAbsent(sign.getDay(), sign);
        signReward.setRewards(actorRewards);
        updateActorSignHistory(signReward);
        return ErrorCode.SERVER_SUCCESS;
    }

    /**
     * 每日奖励信息
     *
     * @param day
     * @param globalVariable
     * @return
     */
    private static RewardBean daily(int day, int globalVariable) {
        RewardBean bean = new RewardBean();
        bean.setDay(day);
        String drop = Misc.cutQuotes(ConfigManager.globalVariableConfMap.get(globalVariable).getValue());
        bean.setDrop(drop);
        if (day == 1) {
            bean.setStatus(StateCode.IN_PROGRESS.value());
            bean.setTakeTick(OnLineTime);
        } else {
            bean.setStatus(StateCode.NOT_STARTED.value());
            bean.setTakeTick(OnLineTime + TimeUtil.MILLIS_IN_DAY * day);
        }
        return bean;
    }

    // Drop奖励分配使用
    public static Map<String, Integer> callable(String reward) {
        Map<String, Integer> rewards = new ConcurrentHashMap<>();
        String[] split = reward.split(",");
        rewards.put(split[0], Integer.parseInt(split[1]));
        return rewards;
    }
}
