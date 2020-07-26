package com.noseparte.game.sign.service.impl;

import com.alibaba.fastjson.JSON;
import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.common.resources.SignRewardConf;
import com.noseparte.common.utils.DateUtils;
import com.noseparte.game.base.GameUtils;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import com.noseparte.game.sign.entity.SignReward;
import com.noseparte.game.sign.mongo.SignRewardDao;
import com.noseparte.game.sign.service.SignRewardService;
import jodd.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SignRewardServiceImpl implements SignRewardService {

    @Resource
    private SignRewardDao signRewardDao;
    @Autowired
    private RoleService roleService;
    @Resource
    RedissonClient redissonClient;

    // 开服时间  ConfigManager.globalVariableMap.get(1033).toString()
    private static long OnLineTime = DateUtils.stringToDate(Misc.getStringVariable(1055), DateUtils.DATE_PATTERN).getTime();

    @Override
    public void initSignRewardMgr(Long rid) {
        Map<Integer, SignRewardConf> actorRewards = ConfigManager.SignRewardConfMap;
        Map<Integer, RewardBean> rewards = new HashMap<>(7);
        RewardBean rewardBean;
        for(SignRewardConf sign : actorRewards.values()){
            Integer day = sign.getId();
            rewardBean = daily(day);
            rewards.putIfAbsent(day, rewardBean);
        }
        SignReward sign = new SignReward();
        sign.setRid(rid);
        sign.setRewards(rewards);
        this.addSign(sign);
    }

    @Override
    public SignReward getSign(Long rid) {
        SignReward signReward = RedissonUtils.get(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN + rid, SignReward.class);
        if (null != signReward) {
            return signReward;
        }
        signReward = signRewardDao.getSignHistoryByRole(rid);
        if (null != signReward) {
            try {
                if (RedissonUtils.lock(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN + rid)) {
                    RedissonUtils.set(signReward, redissonClient,KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN + rid, KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN_EXPIRE_TIME);
                }
            } finally {
                RedissonUtils.unlock(redissonClient, KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN + rid);
            }
        }
        return signReward;
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
        SignReward signReward = getSign(rid);
        if (Objects.isNull(signReward)) {
            return ErrorCode.SIGN_ERROR;
        }
        Map<Integer, RewardBean> actorRewards = signReward.getRewards();
        RewardBean sign = actorRewards.values().stream()
                .filter(rewardBean -> rewardBean.getDay() == day).findFirst().get();
        // 判断领取时间
        long remaining = System.currentTimeMillis() - OnLineTime;
        int today = remaining < 0L ? -1 : (int) (remaining / (3600*1000*24));
        if (today+1 >= day && sign.getStatus() != StateCode.COMPLETED.value()) {
            // 发放奖励
            List<HoldItem> holdItems = GameUtils.getHoldItems(actor, sign.getDrop(), rid);

            boolean issued = roleService.distributeAwardToActor(actor, holdItems);
            if (issued) {
                log.info("发放奖励成功： role={}, 奖励内容: holdItems={}, 发放时间： distributeTick={}",
                        actor.getRid(), JSON.toJSONString(holdItems), new Date().getTime());
            }
            sign.setStatus(StateCode.COMPLETED.value());
        } else {
            return ErrorCode.REWARD_UNAVAILABLE_TIME;
        }
        actorRewards.put(sign.getDay(), sign);
        signReward.setRewards(actorRewards);
        updateSign(signReward);
        return ErrorCode.SERVER_SUCCESS;
    }

    /**
     * 每日奖励信息
     *
     * @param day 天数
     * @return 签到信息
     */
    private static RewardBean daily(int day) {
        Map<Integer, SignRewardConf> actorRewards = ConfigManager.SignRewardConfMap;
        RewardBean bean = new RewardBean();
        bean.setDay(day);
        String drop = Misc.cutQuotes(actorRewards.get(day).getValue());
        bean.setDrop(drop);
//        if (day == 1) {
//            bean.setStatus(StateCode.IN_PROGRESS.value());
//            bean.setTakeTick(OnLineTime);
//        } else {
//            bean.setStatus(StateCode.NOT_STARTED.value());
//            // 隔天领取
//            bean.setTakeTick(OnLineTime + TimeUtil.MILLIS_IN_DAY * (day-1));
//        }
        bean.setStatus(StateCode.NOT_STARTED.value());
        return bean;
    }

    private boolean updateSign(SignReward signReward) {
        boolean success = signRewardDao.updateActorSignHistory(signReward);
        if(success){
            cacheSign(signReward);
        }
        return success;
    }


    private boolean addSign(SignReward signReward) {
        boolean success = signRewardDao.insertActorReward(signReward);
        if (success) {
            cacheSign(signReward);
        }
        return success;
    }

    private void cacheSign(SignReward signReward) {
        Long rid = signReward.getRid();
        String sign_lock_key = KeyPrefix.GameCoreRedisPrefix.CACHE_SIGN + rid;
        try {
            if (RedissonUtils.lock(redissonClient, sign_lock_key)) {
                RedissonUtils.set(signReward, redissonClient, sign_lock_key, KeyPrefix.GameCoreRedisPrefix.CACHE_USER_EXPIRE_TIME);
            }
        } finally {
            RedissonUtils.unlock(redissonClient, sign_lock_key);
        }
    }

}
