package com.noseparte.game.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.card.entity.Card;
import com.noseparte.game.card.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CardListAction extends Action {

    @Autowired
    private CardService cardService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Card cardPackage = cardService.getCardById(rid);
        return Resoult.ok(RegisterProtocol.CARD_LIST_ACTION_RESP).responseBody(cardPackage);
    }
}
