package com.liema.sdk.internal.service;

import com.liema.sdk.internal.entity.Account;

public interface AccountService {

    boolean register(Account account);

    Account login(Account account);
}
