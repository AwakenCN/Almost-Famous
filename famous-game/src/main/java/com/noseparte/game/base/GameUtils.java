package com.noseparte.game.base;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.PropBean;
import com.noseparte.common.bean.protocol.RoleBean;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.role.entity.Role;

import java.util.*;

public class GameUtils {

    public static List<PropBean> getPropList(List<HoldItem> holdItems) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        Set<HoldItem> set = new HashSet<>(holdItems);
        for (HoldItem item : set) {
            long count = holdItems.stream().filter(holdItem -> holdItem.getItemId() == item.getItemId()).count();
            map.put(item.getItemId(), (int) count);
        }
        List<PropBean> propList = new ArrayList<>();
        PropBean prop = null;
        for (HoldItem item : set) {
            prop = new PropBean();
            prop.setItemId(item.getItemId());
            prop.setType(item.getCode());
            if (item.getCode() > AttrCode.EXP.value) {
                prop.setNum(map.get(item.getItemId()));
            } else {
                prop.setNum(item.getNum());
            }
            propList.add(prop);
        }
        return propList;
    }


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

    public static Map<String, Object> getActorCurrencyData(Role role, Long rid) {
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
        return data;
    }
}
