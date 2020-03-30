package com.noseparte.login.sdk.internal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.db.service.GeneralServiceImpl;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.utils.TokenGenerator;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.mongo.AccountDao;
import com.noseparte.login.sdk.internal.service.AccountService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 18:15
 * @Description
 */
@Service("accountService")
public class AccountServiceImpl extends GeneralServiceImpl<Account> implements AccountService {

    @Resource
    AccountDao accountDao;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RpcClient rpcClient;

    @Override
    public boolean register(Account account) {
        String userName = account.getAccount();
        String password = account.getPassword();
        account = accountDao.getOne(account);
        if (Objects.isNull(account)) {
            long uid = rpcClient.getUniqueId();
            account = new Account();
            account.setUid(uid);
            account.setAccount(userName);
            account.setPassword(password);
            account.setCreateTime(new Date());
            try {
                accountDao.insert(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public Account login(Account account, String clientIp) {
        account = accountDao.getOne(account);
        if (Objects.nonNull(account)) {
            String access_token = TokenGenerator.getToken(account.getUid());
            String refreshToken = TokenGenerator.getToken(account.getUid());
            accountDao.update(account);
            RBucket<Object> bucket = redissonClient.getBucket(KeyPrefix.AdminRedisPrefix.ADMIN_USER_ID + account.getUid());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", access_token);
            jsonObject.put("ip", clientIp);
            jsonObject.put("refresh_token", refreshToken);
            jsonObject.put("current_time", System.currentTimeMillis());
            jsonObject.put("expire_tick", System.currentTimeMillis() + 1800 * 1000);
            jsonObject.put("refresh_tick", System.currentTimeMillis() + 600 * 1000);
            String token = jsonObject.toJSONString();
            account.setToken(token);
            bucket.set(token);
        }
        return account;
    }

    @Override
    public Account refreshAccessToken(Long uid, String clientIp, String refresh_token) {
        Account account = accountDao.findById(uid);
        assert account != null;
        String key = KeyPrefix.AdminRedisPrefix.ADMIN_USER_ID + uid;
        String token = RedissonUtils.get(redissonClient, key, String.class);
        if (null == token) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(token);
        String refresh_token_redis = jsonObject.getString("refresh_token");
        Long refresh_tick = jsonObject.getLong("refresh_tick");
        Long expire_tick = jsonObject.getLong("expire_tick");
        long now = System.currentTimeMillis();
        if (refresh_token_redis.equals(refresh_token)) {
            if (now < refresh_tick) {
                account.setToken(token);
                return account;
            } else if (now < expire_tick) {
                // effect n.效果 effectively adj.有效地
                JSONObject effectively = new JSONObject();
                String effect_token = TokenGenerator.getToken(uid);
                effectively.put("current_time", System.currentTimeMillis());
                effectively.put("access_token", effect_token);
                effectively.put("ip", clientIp);
                effectively.put("refresh_token", refresh_token);
                effectively.put("refresh_tick", System.currentTimeMillis() + 600 * 1000);
                effectively.put("expire_tick", System.currentTimeMillis() + 1800 * 1000);
                String new_token = effectively.toJSONString();
                RedissonUtils.set(new_token, redissonClient, key, 1800);
                account.setToken(new_token);
                return account;
            }
        }
        return null;
    }

}
