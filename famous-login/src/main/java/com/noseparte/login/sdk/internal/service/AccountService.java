package com.noseparte.login.sdk.internal.service;

import com.noseparte.common.db.service.GeneralService;
import com.noseparte.login.sdk.internal.entity.Account;

public interface AccountService extends GeneralService<Account> {

    boolean register(Account account);

    Account login(Account account, String clientIp);

    Account refreshAccessToken(Long uid, String clientIp, String refresh_token);
}
