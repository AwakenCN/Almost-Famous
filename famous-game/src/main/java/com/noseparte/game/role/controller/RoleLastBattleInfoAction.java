package com.noseparte.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 获得最后一次匹配出战信息
 */
@Slf4j
@Component
public class RoleLastBattleInfoAction extends Action {

    @Autowired
    RoleService roleService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Role role = roleService.selectByRoleId(rid);
        Map<String, Object> params = new HashMap<>();
        params.put("rid", rid);
        params.put("lastBattleSchool", role.getLastBattleSchool());
        params.put("lastBattleCardGroup", role.getLastBattleCardGroup());
        return Resoult.ok(RegisterProtocol.ROLE_LAST_BATTLE_INFO_ACTION_RESP).responseBody(params);
    }
}
