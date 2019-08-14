package com.liema.sdk.internal.mongo;

import com.liema.sdk.internal.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 18:10
 * @Description
 */
@Slf4j
@Repository
public class AccountDao {

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

    public boolean save(Account account) {
        Account actor = accountMongoTemplate.insert(account);
        if(Objects.nonNull(actor)){
            return true;
        }
        return false;
    }
}
