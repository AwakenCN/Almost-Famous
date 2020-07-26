package com.noseparte.game.base;

import com.noseparte.common.bean.*;
import com.noseparte.common.bean.protocol.RoleBean;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.global.Resoult;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.pack.entity.ActorBag;
import com.noseparte.game.role.entity.Role;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameUtils {

    /**
     * 克隆背包内的所有物品
     *
     * @param pack
     * @return
     */
    protected static Map<Integer, BagBean> cloneItems(ActorBag pack) {
        Map<Integer, BagBean> itemMap = new ConcurrentHashMap<Integer, BagBean>();
        for (BagBean a : pack.packages.values()) {
            BagBean h = cloneItem(a);
            itemMap.put(h.getItemId(), h);
        }
        return itemMap;
    }

    /**
     * 克隆一件物品
     *
     * @param a
     * @return
     */
    protected static BagBean cloneItem(BagBean a) {
        BagBean target = new BagBean();
        BeanUtils.copyProperties(a, target);
        return target;
    }

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

    public static List<HoldItem> getHoldItems(Role role, String drop, Long rid) {
        Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
        String quotes = Misc.cutQuotes(drop);
        String[] drops = Misc.split(quotes, "\\,");
        ItemConf itemConf = itemConfMap.get(Integer.parseInt(drops[0]));
        List<HoldItem> holdItems = ItemMgr.dropItem(itemConf.getDrop());
        return holdItems;
    }

    public static Resoult getActorCurrency(Role role, Long rid) {
        Map<String, Object> data = getData(role, rid);
        return Resoult.ok(RegisterProtocol.ROLE_ATTR_ACTION_RESP).responseBody(data);
    }

    public static Map<String, Object> getActorCurrencyData(Role role, Long rid) {
        Map<String, Object> data = getData(role, rid);
        return data;
    }

    private static Map<String, Object> getData(Role role, Long rid) {
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

    public static List<BagBean> getBagList(List<HoldItem> packages) {
        List<BagBean> bag = new ArrayList<>();
        BagBean bean;
        for (HoldItem item : packages) {
            bean = new BagBean();
            bean.setItemId(item.getItemId());
            bean.setNum(item.getNum());
            bag.add(bean);
        }
        return bag;
    }

    public static Map<String, Object> getCardList(Role role, long rid, List<CardBean> cards) {
        Map<String, Object> data = new HashMap<>();
        data.put("rid", rid);
        data.put("attrId", AttrCode.SILVER.value());
        data.put("attrVal", role.getSilver());
        data.put("cards", cards);
        return data;
    }

}
