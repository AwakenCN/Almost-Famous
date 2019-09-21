package com.noseparte.game.mission.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.mission.entity.Mission;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hyt
 * @since 2019-06-27
 */
public interface MissionDao extends GeneralDao<Mission> {

    Mission getActorMissionsByRole(Long rid);

    void updateActorMission(Mission mission);

    void insertActorMission(Mission mission);
}
