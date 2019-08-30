package com.liema.game.pack.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.pack.entity.ActorBag;
import com.liema.game.pack.service.BagCardStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CardBagListAction extends Action {

    @Autowired
    private BagCardStrategyService iBagCardStrategyService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        ActorBag backpack = iBagCardStrategyService.getSpecificBackpack(rid);
        return Resoult.ok(RegisterProtocol.CARD_BAG_LIST_RESP).responseBody(backpack);
    }
}
