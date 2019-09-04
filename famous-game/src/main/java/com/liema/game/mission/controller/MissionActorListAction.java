package com.liema.game.mission.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liema.common.bean.MissionBean;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.mission.entity.Mission;
import com.liema.game.mission.service.MissionService;
import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MissionActorListAction extends Action {

    @Autowired
    MissionService iMissionService;
    @Autowired
    RoleService iRoleService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        log.info(" { 请求玩家当前任务列表 Request: json={} } ", JSON.toJSONString(jsonObject));
        Map<Integer, MissionBean> actorMissions = new ConcurrentHashMap<>();
        Long rid = jsonObject.getLong("rid");
        Mission mission = iMissionService.getActorMissionById(rid);
        if (Objects.isNull(mission)) {
            return Resoult.error(RegisterProtocol.MISSION_ACTOR_LIST_RESP, ErrorCode.MISSION_NOT_EXIST, "获取任务列表失败");
        }
        Role role = iRoleService.selectByRoleId(rid);
        if (Objects.isNull(role)) {
            return Resoult.error(RegisterProtocol.MISSION_ACTOR_LIST_RESP, ErrorCode.ACCOUNT_NOT_EXIST, "");
        }
        mission = iMissionService.actorMissionMgr(mission, role);
        return Resoult.ok(RegisterProtocol.MISSION_ACTOR_LIST_RESP).responseBody(mission);
    }
}
