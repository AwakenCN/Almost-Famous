package com.liema.game.pack.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.bean.Action;
import com.liema.common.bean.CardBean;
import com.liema.common.bean.ErrorCode;
import com.liema.common.bean.Resoult;
import com.liema.common.utils.FastJsonUtils;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.base.SendMessage;
import com.liema.game.item.HoldItem;
import com.liema.game.mission.service.MissionService;
import com.liema.game.pack.entity.ActorBag;
import com.liema.game.pack.service.BagCardStrategyService;
import com.liema.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class CardBagSelectAction extends Action {

    @Autowired
    private BagCardStrategyService iBagCardStrategyService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MissionService missionService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        String packagesData = jsonObject.getString("packages");
        List<HoldItem> packages = FastJsonUtils.toList(packagesData, HoldItem.class);
        ActorBag actorBag = iBagCardStrategyService.getSpecificBackpack(rid);
        List<CardBean> cards = iBagCardStrategyService.selectCardPack(actorBag, packages);
        if (Objects.isNull(cards)) {
            return Resoult.error(RegisterProtocol.CARD_BAG_SELECT_ACTION_RESP, ErrorCode.SERVER_ERROR, "");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("cards", cards);

        missionService.actorMissionMgr(missionService.getActorMissionById(rid), roleService.selectByRoleId(rid));
        return Resoult.ok(RegisterProtocol.CARD_BAG_SELECT_ACTION_RESP).responseBody(data);
    }
}
