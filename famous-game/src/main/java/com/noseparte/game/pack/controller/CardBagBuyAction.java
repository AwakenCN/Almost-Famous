package com.noseparte.game.pack.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.BagBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.pack.service.BagCardStrategyService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
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
public class CardBagBuyAction extends Action {

    @Autowired
    private BagCardStrategyService iBagCardStrategyService;
    @Autowired
    private RoleService roleService;
    @Autowired
    SendMessage sendMessage;
    @Autowired
    MissionService missionService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Long rid = jsonObject.getLong("rid");
        String packagesData = jsonObject.getString("packages");
        List<HoldItem> packages = FastJsonUtils.toList(packagesData, HoldItem.class);
        Role role = roleService.selectByRoleId(rid);
        ErrorCode errorCode = iBagCardStrategyService.buyCardPack(role, packages);
        if (errorCode != ErrorCode.SERVER_SUCCESS) {
            return Resoult.error(RegisterProtocol.CARD_BAG_BUY_ACTION_RESP, errorCode, "");
        }
        Role actor = roleService.selectByRoleId(rid);
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("attrId", AttrCode.DIAMOND.value());
        data.put("attrVal", actor.getDiamond());
        List<BagBean> bag = new ArrayList<>();
        BagBean bean = null;
        for (HoldItem item : packages) {
            bean = new BagBean();
            bean.setItemId(item.getItemId());
            bean.setNum(item.getNum());
            bag.add(bean);
        }
        data.put("packages", bag);

        missionService.actorMissionMgr(missionService.getActorMissionById(rid), roleService.selectByRoleId(rid));
        //任务推送
//        missionService.noticeMission(actor.getRid());
//        sendMessage.send(rid, Resoult.ok(RegisterProtocol.CARD_BAG_BUY_ACTION_RESP).responseBody(data));
        return Resoult.ok(RegisterProtocol.CARD_BAG_BUY_ACTION_RESP).responseBody(data);
//        return sendMessage.sendNow(rid);
    }

}
