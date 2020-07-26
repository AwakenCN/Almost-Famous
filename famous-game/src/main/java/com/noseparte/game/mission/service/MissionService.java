package com.noseparte.game.mission.service;

import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.mission.entity.Mission;
import com.noseparte.game.role.entity.Role;

public interface MissionService {

    void initMission(Long rid);

    Mission getMission(Long rid);

    Mission getCurrentMission(Long rid);

    void completionMission(Long rid, MissionBean missionBean);

    boolean addMission(Mission mission);

    boolean updateMission(Mission mission);

    Mission actorMissionMgr(Role role, Mission mission, Integer missionType, Integer model);

    ErrorCode receiveAwardMission(Long rid, Integer missionId);
}
