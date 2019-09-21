package com.noseparte.game.card.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.card.entity.Card;

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
