package com.noseparte.game.mission.service;

import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.mission.entity.Mission;
import com.noseparte.game.role.entity.Role;

public interface MissionService {

    void initMission(Long rid);

    Mission getActorMissionById(Long rid);

    void updateActorMission(Long rid, Integer missionId, boolean pass);

    void updateRoleMission(Mission mission);

    Mission actorMissionMgr(Mission mission, Role role);

    void noticeMission(Long rid);

    ErrorCode receivedMission(Long rid, Integer missionId);
}
