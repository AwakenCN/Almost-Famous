package com.noseparte.game.pack.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.pack.entity.ActorBag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 11:22
 * @Description
 */
@Repository
public class ActorBagDaoImpl extends GeneralDaoImpl<ActorBag> implements ActorBagDao {

    @Override
    protected Class<ActorBag> getEntityClass() {
        return ActorBag.class;
    }

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    public void updateActorBag(ActorBag pack) {
        Query query = new Query(Criteria.where("rid").is(pack.getRid()));
        Update update = new Update()
                .set("packages", pack.getPackages())
                .set("buyCount", pack.getBuyCount())
                .set("selectCount", pack.getSelectCount())
                .set("probability", pack.getProbability());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }

    @Override
    public void insertActorBag(ActorBag bag) {
        gameMongoTemplate.insert(bag);
    }

    @Override
    public ActorBag getSpecificBackpack(Long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }
}
