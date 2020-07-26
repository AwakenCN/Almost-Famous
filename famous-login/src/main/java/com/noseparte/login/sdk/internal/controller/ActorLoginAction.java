package com.noseparte.login.sdk.internal.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.login.sdk.dispatch.RegisterProtocol;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.service.AccountService;
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
        String clientIp = request.getRemoteAddr();
        account = accountService.login(account, clientIp);

        if (Objects.nonNull(account)) {
            return Resoult.ok(RegisterProtocol.ACTOR_LOGIN_ACTION_RESP).responseBody(account);
        }
        return Resoult.error(RegisterProtocol.ACTOR_LOGIN_ACTION_RESP, ErrorCode.ACCOUNT_NOT_EXIST, "");
    }

}
