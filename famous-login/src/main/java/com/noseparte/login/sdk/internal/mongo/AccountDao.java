package com.noseparte.login.sdk.internal.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.login.sdk.internal.entity.Account;

/**
 * @author Noseparte
 * @date 2019/8/12 18:10
 * @Description
 */
public interface AccountDao extends GeneralDao<Account> {

    Account getOne(Account account);

    void update(Account account);

    Account findById(Long uid);
}
