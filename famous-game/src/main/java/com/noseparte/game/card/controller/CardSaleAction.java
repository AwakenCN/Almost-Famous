package com.noseparte.game.card.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
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
public class CardSaleAction extends Action {

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
        ErrorCode errorCode = cardService.sale(rid, cards);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return Resoult.error(RegisterProtocol.CARD_SALE_ACTION_RESP, errorCode, "");
        }
        Role role = roleService.selectByRoleId(rid);
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("attrId", AttrCode.SILVER.value());
        data.put("attrVal", role.getSilver());
        data.put("cards", cards);

        sendMessage.send(rid, Resoult.ok(RegisterProtocol.ROLE_ATTR_ACTION_RESP).responseBody(data));
        return Resoult.ok(RegisterProtocol.CARD_SALE_ACTION_RESP).responseBody(data);
    }
}
