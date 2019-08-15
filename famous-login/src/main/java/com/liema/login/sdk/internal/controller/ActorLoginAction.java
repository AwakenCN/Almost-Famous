package com.liema.login.sdk.internal.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.login.sdk.internal.service.AccountService;
import com.liema.login.sdk.dispatch.RegisterProtocol;
import com.liema.login.sdk.internal.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 17:43
 * @Description
 */
@Component
public class ActorLoginAction extends Action {

    @Autowired
    private AccountService accountService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Account account = jsonObject.toJavaObject(Account.class);
        account = accountService.login(account);

        if (Objects.nonNull(account)) {
            return Resoult.ok(RegisterProtocol.ACTOR_LOGIN_ACTION_RESP);
        }
        return Resoult.error(RegisterProtocol.ACTOR_LOGIN_ACTION_RESP, ErrorCode.SERVER_ERROR, "");
    }

}
