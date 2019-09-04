package com.liema.login.sdk.internal.service;

import com.liema.login.sdk.internal.entity.Account;

public interface AccountService {

    boolean register(Account account);

    Account login(Account account);
}
