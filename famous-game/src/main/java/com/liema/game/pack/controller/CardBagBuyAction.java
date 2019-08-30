package com.liema.game.pack.controller;

import com.alibaba.fastjson.JSONObject;
import com.liema.common.bean.AttrCode;
import com.liema.common.bean.BagBean;
import com.liema.common.exception.ErrorCode;
import com.liema.common.global.Action;
import com.liema.common.global.Resoult;
import com.liema.common.utils.FastJsonUtils;
import com.liema.game.base.RegisterProtocol;
import com.liema.game.base.SendMessage;
import com.liema.game.item.HoldItem;
import com.liema.game.mission.service.MissionService;
import com.liema.game.pack.service.BagCardStrategyService;
import com.liema.game.role.entity.Role;
import com.liema.game.role.service.RoleService;
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
