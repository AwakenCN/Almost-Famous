package com.noseparte.game.sign.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.PropBean;
import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.game.base.GameUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.sign.entity.SignReward;
import com.noseparte.game.sign.service.SignRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class SignRewardListAction extends Action {

    @Autowired
    private SignRewardService iSignRewardService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Resoult res = null;
        Long rid = jsonObject.getLong("rid");
        SignReward signReward = iSignRewardService.getSignHistoryByRole(rid);
        if (Objects.isNull(signReward)) {
            return res.error(RegisterProtocol.SIGN_REWARD_LIST_RESP, ErrorCode.SIGN_ERROR, "获取签到信息失败");
        }

        JSONObject data = new JSONObject();
        Map<Integer, Object> result = new LinkedHashMap();
        Map<Integer, RewardBean> actorRewards = signReward.getRewards();
        for (RewardBean actor : actorRewards.values()) {
            Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
            String quotes = Misc.cutQuotes(actor.getDrop());
            String[] drops = Misc.split(quotes, "\\,");
            ItemConf itemConf = itemConfMap.get(Integer.parseInt(drops[0]));
            List<HoldItem> holdItems = ItemMgr.dropItem(itemConf.getDrop());
            List<PropBean> propList = GameUtils.getPropList(holdItems);
            JSONObject object = new JSONObject();
            object.put("day", actor.getDay());
            object.put("status", actor.getStatus());
            object.put("drop", propList);
            object.put("takeTick", actor.getTakeTick());
            object.put("signTick", actor.getSignTick());
            // 判断是否是可领取状态
            if (actor.getTakeTick() < new Date().getTime() && actor.getStatus() != StateCode.COMPLETED.value()) {
                actor.setStatus(StateCode.IN_PROGRESS.value());
                object.put("status", actor.getStatus());
            }
            result.put(actor.getDay(), object);
        }
        data.put("rid", signReward.getRid());
        data.put("rewards", result);
        return res.ok(RegisterProtocol.SIGN_REWARD_LIST_RESP).responseBody(data);
    }

}
