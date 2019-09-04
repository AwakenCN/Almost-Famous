package com.liema.game.card.service;

import com.liema.common.bean.CardBean;
import com.liema.common.exception.ErrorCode;
import com.liema.game.card.entity.Card;

import java.util.List;

/**
 * <p>
 * 卡包 服务类
 * </p>
 *
 * @author liang
 * @since 2019-03-18
 */
public interface CardService {

    void initCard(Long rid);

    ErrorCode buy(Long rid, List<CardBean> cards);

    ErrorCode sale(Long rid, List<CardBean> cards);

    Card getCardById(Long rid);

    boolean lock(Long rid, List<Integer> cardIds, Integer state);

    void updateCard(Card cardPackage);

    ErrorCode storeCardPackage(Card card, List<CardBean> cards);
}
