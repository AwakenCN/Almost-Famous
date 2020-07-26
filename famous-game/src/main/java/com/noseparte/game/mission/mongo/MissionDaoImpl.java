package com.noseparte.game.mission.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.mission.entity.Mission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 11:44
 * @Description
 */
@Repository
public class MissionDaoImpl extends GeneralDaoImpl<Mission> implements MissionDao {

    @Override
    protected Class<Mission> getEntityClass() {
        return Mission.class;
    }

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    public Mission getActorMissionsByRole(Long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }

    @Override
    public void updateActorMission(Mission mission) {
        Query query = new Query(Criteria.where("rid").is(mission.getRid()));
        Update update = new Update().set("missions", mission.getMissions());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }

    @Override
    public void insertActorMission(Mission mission) {
        gameMongoTemplate.insert(mission);
    }
}
