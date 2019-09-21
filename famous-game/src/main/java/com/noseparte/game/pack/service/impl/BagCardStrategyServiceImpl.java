package com.noseparte.game.pack.service.impl;

import com.noseparte.common.bean.*;
import com.noseparte.common.bean.protocol.GoodBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.Misc;
import com.noseparte.common.resources.CardConf;
import com.noseparte.common.resources.GlobalVariableConf;
import com.noseparte.common.resources.ItemConf;
import com.noseparte.game.card.entity.Card;
import com.noseparte.game.card.service.CardService;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.pack.entity.ActorBag;
import com.noseparte.game.pack.mongo.ActorBagDao;
import com.noseparte.game.pack.service.BagCardStrategyService;
import com.noseparte.game.role.entity.Role;
import com.noseparte.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class BagCardStrategyServiceImpl implements BagCardStrategyService {

    @Autowired
    private RoleService iRoleService;
    @Resource
    ActorBagDao actorBagDao;
    @Autowired
    CardService cardService;
    @Autowired
    MissionService missionService;

    @Override
    public void initActorBag(Long rid) {
        ActorBag card = new ActorBag();
        card.setRid(rid);
        card.setPackages(new HashMap<>());
        card.setProbability(0);
        card.setBuyCount(0);
        card.setSelectCount(0);
        // card strategy
        actorBagDao.insertActorBag(card);
    }

    @Override
    public ErrorCode putItemsAnyway(ActorBag pack, List<HoldItem> getItems) {
        if (Objects.isNull(pack)) return ErrorCode.SERVER_ERROR;
        if (getItems.isEmpty()) return ErrorCode.SERVER_ERROR;
        for (HoldItem getItem : getItems) {
            putItem(pack, getItem);
        }
        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public ErrorCode putItem(ActorBag pack, HoldItem getItem) {
        if (Objects.isNull(pack)) return ErrorCode.SERVER_ERROR;
        if (getItem.getCode() != AttrCode.CARD_PACKAGE.value) return ErrorCode.SERVER_ERROR;
        if (Objects.isNull(pack.getPackages()) || !pack.packages.containsKey(getItem.itemId)) {
            Map packages = pack.getPackages();
            if (Objects.isNull(packages)) {
                packages = new HashMap();
            }
            BagBean bag = new BagBean();
            BeanUtils.copyProperties(getItem, bag);
            packages.putIfAbsent(getItem.itemId, bag);
            pack.setPackages(packages);
        } else {
            BagBean item = pack.packages.get(getItem.itemId);
            item.setNum(item.num + getItem.num);
            pack.getPackages().put(getItem.itemId, item);
        }
        actorBagDao.updateActorBag(pack);
        return ErrorCode.SERVER_SUCCESS;
    }

    @Override
    public ActorBag getSpecificBackpack(Long rid) {
        ActorBag actorBag = actorBagDao.getSpecificBackpack(rid);
        return actorBag;
    }

    /**
     * 抽卡
     *
     * @param bag
     * @param packages
     * @return
     */
    @Override
    public List<CardBean> selectCardPack(ActorBag bag, List<HoldItem> packages) {
        List<CardBean> cards = new ArrayList<>();
        // 判断背包是否有足够库存
        if (!hasThose(bag, packages)) return null;
        CardBean cardBean = null;
        for (HoldItem card : packages) {
            int itemId = card.getItemId();
            int num = card.getNum();
            Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
            ItemConf itemConf = itemConfMap.get(itemId);

            for (int i = 0; i < num; i++) {
                List<HoldItem> holdItems = ItemMgr.dropItem(itemConf.getDrop());
                for (HoldItem item : holdItems) {
                    cardBean = newCard(item);
                    cards.add(cardBean);
                }
            }
            //判断是否有橙卡
            boolean hit = haveOrangeCard(cards);
            if (hit) {
                bag.setProbability(0);
            } else {
                //橙卡保底值
                GlobalVariableConf guaranteedConfig = ConfigManager.globalVariableConfMap.get(1036);
                Integer guaranteed = Integer.parseInt(guaranteedConfig.getValue());
                //概率值增加
                String increment = itemConf.getIncrement();
                if (Objects.nonNull(increment)) {
                    String[] prob = Misc.cutQuotes(increment).split(",");
                    int probability = Misc.random(Integer.parseInt(prob[0]), Integer.parseInt(prob[1]));
                    //未命中概率递增
                    int nowProb = bag.getProbability() + probability;
                    //随机替换一张普卡为橙卡
                    if (nowProb >= guaranteed) {
                        String specialDrop = itemConf.getSpecialDrop();
                        HoldItem orangeItem = ItemMgr.dropItem(specialDrop).get(0);
                        int index = Misc.random(0, cards.size() - 1);
                        cards.remove(cards.get(index));
                        CardBean orangeCard = newCard(orangeItem);
                        cards.add(orangeCard);
                        //概率清零
                        bag.setProbability(0);
                    } else {
                        bag.setProbability(nowProb);
                    }
                }
            }
            bag.setSelectCount(Misc.increase(bag.getSelectCount(), 1));
            BagBean holdItem = bag.getPackages().get(itemId);
            if (holdItem.getNum() > num) {
                holdItem.setNum(holdItem.getNum() - num);
                bag.getPackages().putIfAbsent(itemId, holdItem);
            } else {
                bag.getPackages().remove(itemId);
            }
        }
        actorBagDao.updateActorBag(bag);
        //存到卡牌背包
        Card cardPackage = cardService.getCardById(bag.getRid());
        cardService.storeCardPackage(cardPackage, cards);
        // List打乱顺序
        Collections.shuffle(cards);
        return cards;
    }

    private CardBean newCard(HoldItem orangeItem) {
        CardBean orangeCard = new CardBean();
        orangeCard.setCardId(orangeItem.getItemId());
        orangeCard.setNum(orangeItem.getNum());
        Map<Integer, CardConf> cardConfMap = ConfigManager.cardConfMap;
        CardConf cardConf = cardConfMap.get(orangeItem.getItemId());
        if (Objects.nonNull(cardConf) && cardConf.getQuality() == 3) {
            orangeCard.setClock(1);
        }
        return orangeCard;
    }

    private boolean haveOrangeCard(List<CardBean> cards) {
        long count = cards.stream().filter(card -> card.getClock() == 1).count();
        return count > 0;
    }

    private boolean hasThose(ActorBag bag, List<HoldItem> packages) {
        if (packages.isEmpty() || Objects.isNull(bag.getPackages())) {
            return false;
        }
        Map<Integer, BagBean> items = bag.getPackages();
        for (HoldItem holdItem : packages) {
            if (items.containsKey(holdItem.getItemId())) {
                BagBean item = items.get(holdItem.getItemId());
                if (item.num >= holdItem.getNum()) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 买 ### 卡包
     *
     * @param actor
     * @param packages
     * @return
     */
    @Override
    public ErrorCode buyCardPack(Role actor, List<HoldItem> packages) {
        if (Objects.isNull(actor)) return ErrorCode.ACCOUNT_NOT_EXIST;
        if (packages.isEmpty()) return ErrorCode.SERVER_ERROR;
        int cost = 0;
        for (HoldItem pack : packages) {
            Map<Integer, ItemConf> itemConfMap = ConfigManager.itemConfMap;
            for (ItemConf item : itemConfMap.values()) {
                if (item.getId() == pack.getItemId()) {
                    cost += item.getPrice() * pack.getNum();
                }
            }
        }
        // 消耗钻石的主要途径
        if (actor.getDiamond() >= cost) {
            int success = iRoleService.cost(actor, AttrCode.DIAMOND, -cost, EventCode.CARD_BUY);
            log.info("购买卡包结果： {}", success);
            if (success == StateCode.NOT_STARTED.value()) {//成功
                ActorBag cardBag = getSpecificBackpack(actor.getRid());
                cardBag.setBuyCount(Misc.increase(cardBag.getBuyCount(), 1));
                ErrorCode errorCode = putItemsAnyway(cardBag, packages);

                return errorCode;
            }
            return ErrorCode.INSUFFICIENT_DIAMOND;
        } else {
            return ErrorCode.INSUFFICIENT_DIAMOND;
        }
    }

    @Override
    public ErrorCode buyByStore(Role role, GoodBean goodInfo) {
        HoldItem item = new HoldItem();
        item.setCode(goodInfo.getType());
        item.setItemId(goodInfo.getGoodId());
        item.setNum(1);
        Integer price = goodInfo.getPrice();
        // 道具贩卖商城使用GOLD
        if (role.getDiamond() >= price) {
            int success = iRoleService.cost(role, AttrCode.GOLD, -price, EventCode.CARD_BUY);
            log.info("购买卡包结果： {}", success);
            if (success == StateCode.NOT_STARTED.value()) {//成功
                ActorBag cardBag = getSpecificBackpack(role.getRid());
                ErrorCode errorCode = putItem(cardBag, item);
                return errorCode;
            }
            return ErrorCode.INSUFFICIENT_GOLD;
        } else {
            return ErrorCode.INSUFFICIENT_GOLD;
        }
    }

    @Override
    public boolean totalBuyCount(Long rid, Integer condition) {
        ActorBag cardBag = getSpecificBackpack(rid);
        return cardBag.getBuyCount() >= condition;
    }

    @Override
    public int getTotalBuyCount(Long rid) {
        return getSpecificBackpack(rid).getBuyCount();
    }

    @Override
    public boolean compareSelectCnt(Long rid, Integer condition) {
        ActorBag actor = getSpecificBackpack(rid);
        if (actor.getSelectCount() >= condition) {
            return true;
        }
        return false;
    }

    @Override
    public int getSelectCardTimes(Long rid) {
        ActorBag actor = getSpecificBackpack(rid);
        return actor.getSelectCount();
    }

}
