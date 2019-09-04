package com.liema.game.card.mongo;

import com.liema.game.card.entity.Card;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * <p>
 * 卡包 Dao 接口
 * </p>
 *
 * @author liang
 * @since 2019-03-18
 */
@Repository
public class CardDao {

    @Resource
    @Qualifier("gameMongoTemplate")
    private MongoTemplate gameMongoTemplate;

    public Card getCardById(Long rid) {
        return null;
    }


    public void addCard(Card card) {

    }

    public void updateCard(Card card) {

    }
}
