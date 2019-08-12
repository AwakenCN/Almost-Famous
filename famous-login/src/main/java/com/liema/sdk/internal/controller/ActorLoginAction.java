package com.liema.sdk.internal.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.global.Action;
import com.liema.global.Resoult;
import com.liema.sdk.internal.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        return null;
    }

}
