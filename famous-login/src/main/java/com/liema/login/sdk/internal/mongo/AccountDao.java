package com.liema.login.sdk.internal.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.login.sdk.internal.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 18:10
 * @Description
 */
public interface AccountDao extends GeneralDao<Account> {

    Account getOne(Account account);

    void update(Account account);
}
