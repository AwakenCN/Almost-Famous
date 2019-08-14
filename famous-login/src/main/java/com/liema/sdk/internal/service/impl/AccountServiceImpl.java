package com.liema.sdk.internal.service.impl;

import com.liema.global.KeyPrefix;
import com.liema.rpc.RpcClient;
import com.liema.sdk.internal.entity.Account;
import com.liema.sdk.internal.mongo.AccountDao;
import com.liema.sdk.internal.service.AccountService;
import com.liema.utils.TokenGenerator;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 18:15
 * @Description
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    RpcClient rpcClient;

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
            account.setCreateTime(new Date().getTime());
            return this.accountDao.save(account);
        }
        return false;
    }

    @Override
    public Account login(Account account) {
        account = accountDao.getOne(account);
        if (Objects.nonNull(account)) {
            String token = TokenGenerator.getToken(account.getUid());
            account.setToken(token);
            accountDao.update(account);
            RBucket<Object> bucket = redissonClient.getBucket(KeyPrefix.AdminRedisPrefix.ADMIN_USER_ID + account.getUid());
            bucket.set(token);
        }
        return account;
    }

}
