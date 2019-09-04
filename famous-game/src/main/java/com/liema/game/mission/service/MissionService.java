package com.liema.game.mission.service;

import com.liema.common.exception.ErrorCode;
import com.liema.game.mission.entity.Mission;
import com.liema.game.role.entity.Role;

public interface MissionService {

    void initMission(Long rid);

    Mission getActorMissionById(Long rid);

    void updateActorMission(Long rid, Integer missionId, boolean pass);

    void updateRoleMission(Mission mission);

    Mission actorMissionMgr(Mission mission, Role role);

    void noticeMission(Long rid);

    ErrorCode receivedMission(Long rid, Integer missionId);
}
