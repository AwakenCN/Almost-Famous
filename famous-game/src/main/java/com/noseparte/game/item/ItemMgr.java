package com.noseparte.game.item;

import com.noseparte.common.bean.AttrCode;
import com.noseparte.common.bean.DropCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.CardConf;
import com.noseparte.common.resources.DropItem;
import com.noseparte.common.resources.DropItemConf;
import com.noseparte.common.resources.ItemConf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemMgr {

    /**
     * @param dropId
     * @return
     */
    public static List<HoldItem> dropItem(int dropId) {
        List<HoldItem> list = new ArrayList<>();
        DropItemConf dropItemConf = ConfigManager.dropItemConfMap.get(dropId);
        if (dropItemConf == null) return list;
        int weight = Misc.random(0, Misc.MAX_WEIGHT);
        for (DropItem dropItem : dropItemConf.dropItemList) {
            if ((dropItem.min == dropItem.max) || (weight >= dropItem.min && weight < dropItem.max)) {
                HoldItem item = generate(dropItem);
                list.add(item);
            }
        }
        return list;
    }

    public static List<HoldItem> dropItem(String conf) {
        List<HoldItem> holdItems = new ArrayList<>();
        String[] drops = Misc.split(conf, "\\;");
        for (String drop : drops) {
            String[] item = Misc.split(drop, "\\,");
            int dropId = Integer.parseInt(item[0]);
            int time = Integer.parseInt(item[1]);
            if (dropId < AttrCode.CARD.value) {
                List<HoldItem> holds = ItemMgr.dropItem(dropId);
                for (HoldItem hold : holds) {
                    hold.setNum(time);
                }
                holdItems.addAll(holds);
            } else {
                if (time > 1) {
                    for (int i = 0; i < time; i++) {
                        List<HoldItem> holds = ItemMgr.dropItem(dropId);
                        holdItems.addAll(holds);
                    }
                } else {
                    List<HoldItem> hold = ItemMgr.dropItem(dropId);
                    holdItems.addAll(hold);
                }
            }
        }
        return Objects.nonNull(holdItems) ? holdItems : null;
    }

    /**
     * @param dropItem
     * @param
     * @return
     */
    public static HoldItem generate(DropItem dropItem) {
        HoldItem item = null;
        if (dropItem.getType() == DropCode.CURRENCY) {
            int attr = dropItem.getId();
            if (attr == AttrCode.GOLD.value()) {
                item = new HoldItem(dropItem.getId(), AttrCode.GOLD.value());
            }
            if (attr == AttrCode.SILVER.value()) {
                item = new HoldItem(dropItem.getId(), AttrCode.SILVER.value());
            }
            if (attr == AttrCode.DIAMOND.value()) {
                item = new HoldItem(dropItem.getId(), AttrCode.DIAMOND.value());
            }
        } else if (dropItem.getType() == DropCode.EXP) {
            item = new HoldItem(dropItem.getId(), AttrCode.EXP.value());
        } else if (dropItem.getType() == DropCode.CARD) {
            item = selectCard(dropItem);
        } else if (dropItem.getType() == DropCode.ITEM) {
            item = new HoldItem();
            Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
            for (ItemConf conf : itemConfMap.values()) {
                if (conf.getId() == dropItem.getId()) {
                    item.setItemId(conf.getId());
                    item.setCode(conf.getType());
                    item.setNum(1);
                }
            }
        } else if (dropItem.getType() == DropCode.OTHER) {
            //掉落表组需要递归 dropItem()方法获取掉落组的具体道具
            item = dropItem(dropItem.getId()).stream().findFirst().get();
        } else if (dropItem.getType() == DropCode.SELF) {
            //掉落表组需要递归 dropItem()方法获取掉落组的具体道具
            item = dropItem(dropItem.getId()).stream().findFirst().get();
        }
        return item;
    }

    private static HoldItem selectCard(DropItem dropItem) {
        HoldItem item = new HoldItem();
        Map<Integer, CardConf> cardMap = ConfigManager.cardConfMap;
        for (CardConf card : cardMap.values()) {
            if (card.getId() == dropItem.getId()) {
                item.setItemId(card.getId());
                item.setCode(DropCode.CARD.value());
                item.setQuality(card.getQuality());
                item.setUpLevel(card.getLevel());
                item.setNum(1);
            }
        }
        return item;
    }

}
