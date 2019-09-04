package com.liema.game.mission.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.GameUtils;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.base.SendMessage;
import com.liema.game.mission.entity.Mission;
import com.liema.game.mission.service.MissionService;
import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
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