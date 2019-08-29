package com.liema.game.base;

import com.liema.common.bean.AttrCode;
import com.liema.common.bean.protocol.RoleBean;
import com.liema.common.global.Resoult;
import com.liema.game.role.entity.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameUtils {

    public static Resoult getActorCurrency(Role role, Long rid) {
        List<RoleBean> list = new ArrayList<>();
        RoleBean goldMeta = new RoleBean();
        goldMeta.setRid(rid);
        goldMeta.setAttrId(AttrCode.GOLD.value());
        goldMeta.setAttrVal(role.getGold());
        list.add(goldMeta);
        RoleBean silverMeta = new RoleBean();
        silverMeta.setRid(rid);
        silverMeta.setAttrId(AttrCode.SILVER.value());
        silverMeta.setAttrVal(role.getSilver());
        list.add(silverMeta);
        RoleBean diamondMeta = new RoleBean();
        diamondMeta.setRid(rid);
        diamondMeta.setAttrId(AttrCode.DIAMOND.value());
        diamondMeta.setAttrVal(role.getDiamond());
        list.add(diamondMeta);
        Map<String, Object> data = new HashMap<>();
        data.put("currency", list);
        return new Resoult().ok(RegisterProtocol.ROLE_ATTR_ACTION_RESP).responseBody(data);
    }
}
