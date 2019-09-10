package com.liema.game.card.mongo;

import com.liema.common.db.dao.GeneralDao;
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
public interface CardDao extends GeneralDao<Card> {


    Card getCardById(Long rid);

    void addCard(Card card);

    void updateCard(Card card);

}
