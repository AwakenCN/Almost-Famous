package com.noseparte.login.sdk.internal.service.impl;

import com.noseparte.common.db.service.GeneralServiceImpl;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.utils.TokenGenerator;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.mongo.AccountDao;
import com.noseparte.login.sdk.internal.service.AccountService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 18:15
 * @Description
 */
@Service("accountService")
public class AccountServiceImpl extends GeneralServiceImpl<Account> implements AccountService {

    @Autowired
    AccountDao accountDao;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
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
