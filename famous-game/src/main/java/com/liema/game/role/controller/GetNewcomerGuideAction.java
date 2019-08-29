package com.liema.game.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GetNewcomerGuideAction extends Action {

    @Autowired
    RoleService roleService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Role role = roleService.selectByRoleId(rid);
        Map<String, Integer> data = new HashMap<>();
        data.put("guideId", role.getNewcomerGuide());
        return Resoult.ok(RegisterProtocol.GET_NEWCOMER_GUIDE_ACTION_RESP).responseBody(data);
    }
}
