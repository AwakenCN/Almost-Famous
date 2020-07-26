package com.noseparte.game.pack.service;

import com.noseparte.common.bean.CardBean;
import com.noseparte.common.bean.protocol.GoodBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.pack.entity.ActorBag;
import com.noseparte.game.role.entity.Role;

import java.util.List;

public interface BagCardStrategyService {

    void initActorBag(Long rid);

    ErrorCode putItemsAnyway(ActorBag pack, List<HoldItem> getItems);

    ErrorCode putItem(ActorBag pack, HoldItem getItem);

    ActorBag getSpecificBackpack(Long rid);

    /**
     * 抽卡 (卡包)
     *
     * @param bag
     * @param packages
     * @return
     */
    List<CardBean> selectCardPack(ActorBag bag, List<HoldItem> packages);

    /**
     * 买 ### 卡包
     *
     * @param actor
     * @param packages
     * @return
     */
    ErrorCode buyCardPack(Role actor, List<HoldItem> packages);

    ErrorCode buyByStore(Role role, GoodBean goodInfo);

    boolean totalBuyCount(Long rid, Integer condition);

    int getTotalBuyCount(Long rid);

    boolean compareSelectCnt(Long rid, Integer condition);

    int getSelectCardTimes(Long rid);
}
