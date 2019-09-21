package com.noseparte.game.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.card.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CardLockAction extends Action {

    @Autowired
    private CardService cardService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        String ids = jsonObject.getString("cardIds");
        List<Integer> cardIds = FastJsonUtils.toList(ids, Integer.class);
        Integer state = jsonObject.getInteger("state");
        if (null == cardIds || cardIds.size() == 0) {
            return Resoult.error(RegisterProtocol.CARD_LOCK_ACTION_RESP, ErrorCode.CLIENT_PARAMS_ERROR, "");
        }
        if (state < 0 || state > 1) {
            return Resoult.error(RegisterProtocol.CARD_LOCK_ACTION_RESP, ErrorCode.CLIENT_PARAMS_ERROR, "");
        }
        cardService.lock(rid, cardIds, state);
        List<CardBean> cards = new ArrayList<>();
        CardBean card = null;
        for (Integer cardId : cardIds) {
            card = new CardBean();
            card.setCardId(cardId);
            card.setNum(1);
            card.setClock(state);
            cards.add(card);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("cards", cards);
        return Resoult.ok(RegisterProtocol.CARD_LOCK_ACTION_RESP).responseBody(data);
    }
}
