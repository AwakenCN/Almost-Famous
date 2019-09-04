package com.liema.login.sdk.internal.service;

import com.liema.common.db.service.GeneralService;
import com.liema.login.sdk.internal.entity.Account;

public interface AccountService extends GeneralService<Account> {

    boolean register(Account account);

    Account login(Account account);
}
