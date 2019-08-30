package com.liema.game.mission.mongo;

import com.liema.game.mission.entity.Mission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hyt
 * @since 2019-06-27
 */
public class MissionDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    public Mission getActorMissionsByRole(Long rid) {
        return null;
    }

    public void updateActorMission(Mission mission) {

    }

    public void insertActorMission(Mission mission) {

    }
}
