package com.liema.game.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.card.entity.Card;
import com.liema.game.card.service.CardService;
import com.liema.game.mission.service.MissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
