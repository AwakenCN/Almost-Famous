package com.liema.game.sign.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.GameUtils;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.base.SendMessage;
import com.liema.game.role.service.RoleService;
import com.liema.game.sign.service.SignRewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class SignInAction extends Action {

    @Autowired
    private SignRewardService iSignRewardService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        Integer day = jsonObject.getInteger("day");
        ErrorCode code = iSignRewardService.getSignReward(rid, day);
        if (ErrorCode.SERVER_SUCCESS != code) {
            return Resoult.error(RegisterProtocol.SIGN_IN_ACTION_RESP, code, "");
        }
        //推送GM
//        Resoult result = GameUtils.getActorCurrency(roleService.selectByRoleId(rid), rid);
//        sendMessage.send(rid, result);
//        sendMessage.sendNow(rid);

        Map<String, Object> data = GameUtils.getActorCurrencyData(roleService.selectByRoleId(rid), rid);

        return Resoult.ok(RegisterProtocol.SIGN_IN_ACTION_RESP).responseBody(data);
    }
}
