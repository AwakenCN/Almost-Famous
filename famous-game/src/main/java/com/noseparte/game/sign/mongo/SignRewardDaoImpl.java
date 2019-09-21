package com.noseparte.game.sign.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.sign.entity.SignReward;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 11:03
 * @Description
 */
@Repository
public class SignRewardDaoImpl extends GeneralDaoImpl<SignReward> implements SignRewardDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    protected Class<SignReward> getEntityClass() {
        return SignReward.class;
    }

    @Override
    public SignReward getSignHistoryByRole(Long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }

    @Override
    public void insertActorReward(SignReward sign) {
        gameMongoTemplate.insert(sign);
    }

    @Override
    public void updateActorSignHistory(SignReward signReward) {
        Query query = new Query(Criteria.where("rid").is(signReward.getRid()));
        Update update = new Update().set("rewards", signReward.getRewards());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }
}
