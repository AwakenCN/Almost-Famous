package com.noseparte.game.card.service;

import com.noseparte.common.bean.CardBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.card.entity.Card;

import java.util.List;

public interface CardService {

    void initCard(Long rid);

    ErrorCode buy(Long rid, List<CardBean> cards);

    ErrorCode sale(Long rid, List<CardBean> cards);

    Card getCardById(Long rid);

    boolean lock(Long rid, List<Integer> cardIds, Integer state);

    void updateCard(Card cardPackage);

    ErrorCode storeCardPackage(Card card, List<CardBean> cards);
}
