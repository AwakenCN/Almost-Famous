package com.noseparte.game.mission.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.GameUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.mission.entity.Mission;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MissionFinishAction extends Action {

    @Autowired
    private MissionService iMissionService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        Integer missionId = jsonObject.getInteger("missionId");
        ErrorCode code = iMissionService.receivedMission(rid, missionId);

        //推送GM
        Resoult result = GameUtils.getActorCurrency(roleService.selectByRoleId(rid), rid);
        sendMessage.send(rid, result);
//        sendMessage.sendNow(rid);

        if (ErrorCode.SERVER_SUCCESS != code) {
            return Resoult.error(RegisterProtocol.MISSION_FINISH_ACTION_RESP, code, "");
        }
        Mission mission = iMissionService.getActorMissionById(rid);
        Role role = roleService.selectByRoleId(rid);
        mission = iMissionService.actorMissionMgr(mission, role);
        return Resoult.ok(RegisterProtocol.MISSION_FINISH_ACTION_RESP).responseBody(mission);
    }

}