package com.noseparte.login.sdk.internal.mongo.impl;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.mongo.AccountDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/2 11:03
 * @Description
 */
@Slf4j
@Repository
public class AccountDaoImpl extends GeneralDaoImpl<Account> implements AccountDao {

    @Override
    protected Class<Account> getEntityClass() {
        return Account.class;
    }

    @Resource
    @Qualifier("accountMongoTemplate")
    private MongoTemplate accountMongoTemplate;

    public Account getOne(Account account) {
        Query query = new Query();
        query.addCriteria(new Criteria()
                .and("account").is(account.getAccount())
                .and("password").is(account.getPassword()));
        return accountMongoTemplate.findOne(query, Account.class);
    }

    public Account findById(Account account) {
        return accountMongoTemplate.findById(account.getUid(), Account.class);
    }

//    public boolean save(Account account) {
//        Account actor = accountMongoTemplate.insert(account);
//        if (Objects.nonNull(actor)) {
//            return true;
//        }
//        return false;
//    }

    public void update(Account account) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(new Criteria()
                .and("account").is(account.getAccount())
                .and("password").is(account.getPassword()));
        update.set("token", account.getToken());
        accountMongoTemplate.updateFirst(query, update, Account.class);
    }

    @Override
    public Account findById(Long uid) {
        Query query = new Query();
        query.addCriteria(new Criteria().and("uid").is(uid));
        return accountMongoTemplate.findOne(query, Account.class);
    }
}
