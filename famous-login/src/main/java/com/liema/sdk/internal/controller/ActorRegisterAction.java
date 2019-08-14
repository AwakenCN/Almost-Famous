package com.liema.sdk.internal.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.exception.ErrorCode;
import com.liema.global.Action;
import com.liema.global.Resoult;
import com.liema.sdk.dispatch.RegisterProtocol;
import com.liema.sdk.internal.entity.Account;
import com.liema.sdk.internal.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noseparte
 * @date 2019/8/12 18:08
 * @Description
 *      <p>注册<p/>
 *      <p>Register<p/>
 */
@Component
public class ActorRegisterAction extends Action {

    @Autowired
    private AccountService accountService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Account account = jsonObject.toJavaObject(Account.class);
        if (this.accountService.register(account)) {
            return Resoult.ok(RegisterProtocol.ACTOR_REGISTER_ACTION_RESP);
        }
        return Resoult.error(RegisterProtocol.ACTOR_REGISTER_ACTION_RESP, ErrorCode.SERVER_ERROR, "");
    }
}
