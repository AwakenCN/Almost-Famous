package com.noseparte.login.sdk.internal.controller;/**
 * Created by Enzo Cotter on 2019/12/3.
 */

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.SecurityEnum;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.login.sdk.dispatch.RegisterProtocol;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Noseparte
 * @Date: 2019/12/4 18:28
 * @Description:
 *
 *          <p>客户端</p>
 *          <p>无缝刷新token</p>
 */
@Slf4j
@Component
public class RefreshTokenAction extends Action {

    @Resource
    private AccountService accountService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long uid = jsonObject.getLong("uid");
        String refresh_token = jsonObject.getString("refresh_token");
        String grant_type = jsonObject.getString("grant_type");
        String clientIp = request.getRemoteAddr();
        if (!grant_type.equals(SecurityEnum.Credential.name())) {
            return Resoult.error(RegisterProtocol.REFRESH_TOKEN_ACTION_RESP, ErrorCode.CLIENT_PARAMS_ERROR, "");
        }
        Account account = accountService.refreshAccessToken(uid, clientIp, refresh_token);
        if (null == account) {
            return Resoult.error(RegisterProtocol.REFRESH_TOKEN_ACTION_RESP, ErrorCode.TOKEN_EXPIRE_ERROR, "");
        }
        return Resoult.ok(RegisterProtocol.REFRESH_TOKEN_ACTION_RESP).responseBody(account);
    }
}
