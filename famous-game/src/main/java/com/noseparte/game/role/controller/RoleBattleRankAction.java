package com.noseparte.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
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
 * @date 2019/8/9 12:03
 * @Description
 */
@Slf4j
@Component
public class RoleBattleRankAction extends Action {

    @Autowired
    private RoleService roleService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        Role role = roleService.selectByRoleId(rid);
        if (Objects.isNull(role)) {
            return Resoult.error(RegisterProtocol.ROLE_BATTLE_RANK_ACTION_RESP, ErrorCode.ACCOUNT_NOT_EXIST, "");
        }
        return Resoult.ok(RegisterProtocol.ROLE_BATTLE_RANK_ACTION_RESP).responseBody(role.getBattleRank());
    }
}
