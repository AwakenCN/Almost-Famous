package com.liema.game.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.bean.AttrCode;
import com.liema.common.bean.CardBean;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.common.utils.FastJsonUtils;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.base.SendMessage;
import com.liema.game.card.service.CardService;
import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CardBuyAction extends Action {

    @Autowired
    private CardService cardService;
    @Autowired
    RoleService roleService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        String cardsData = jsonObject.getString("cards");
        List<CardBean> cards = FastJsonUtils.toList(cardsData, CardBean.class);
        ErrorCode errorCode = cardService.buy(rid, cards);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return Resoult.error(RegisterProtocol.CARD_BUY_ACTION_RESP, errorCode, "");
        }
        Role role = roleService.selectByRoleId(rid);
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("attrId", AttrCode.SILVER.value());
        data.put("attrVal", role.getSilver());
        data.put("cards", cards);

        sendMessage.send(rid, Resoult.ok(RegisterProtocol.ROLE_ATTR_ACTION_RESP).responseBody(data));
        return Resoult.ok(RegisterProtocol.CARD_BUY_ACTION_RESP).responseBody(data);
    }
}
