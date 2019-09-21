package com.noseparte.game.card.mongo;

import com.noseparte.common.db.dao.GeneralDaoImpl;
import com.noseparte.game.card.entity.Card;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/9/10 11:15
 * @Description
 */
@Repository
public class CardDaoImpl extends GeneralDaoImpl<Card> implements CardDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    @Override
    protected Class<Card> getEntityClass() {
        return Card.class;
    }

    @Override
    public Card getCardById(Long rid) {
        return gameMongoTemplate.findOne(new Query(Criteria.where("rid").is(rid)), getEntityClass());
    }

    @Override
    public void addCard(Card card) {
        gameMongoTemplate.insert(card);
    }

    @Override
    public void updateCard(Card card) {
        Query query = new Query(Criteria.where("rid").is(card.getRid()));
        Update update = new Update().set("cards", card.getCards());
        gameMongoTemplate.findAndModify(query, update, getEntityClass());
    }
}
