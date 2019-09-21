package com.noseparte.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.GameUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/12 15:35
 * @Description
 */
@Slf4j
@Component
public class RoleCurrencyChangeAction extends Action {

    @Autowired
    RoleService roleService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        Role role = roleService.selectByRoleId(rid);
        if (Objects.isNull(role)) {
            if (log.isInfoEnabled()) {
                log.info("玩家不存在");
            }
            return Resoult.error(RegisterProtocol.ROLE_CURRENCY_CHANGE_ACTION_RESP, ErrorCode.ACCOUNT_NOT_EXIST, "");
        }
        roleService.currencyChangeGM(role);
        //推送GM
        Resoult result = GameUtils.getActorCurrency(roleService.selectByRoleId(rid), rid);
        sendMessage.send(rid, result);
//        sendMessage.sendNow(rid);

        return result;
    }
}
