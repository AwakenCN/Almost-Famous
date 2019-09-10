package com.liema.game.mission.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.game.mission.entity.Mission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

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
