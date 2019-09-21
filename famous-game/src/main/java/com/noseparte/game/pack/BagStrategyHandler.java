package com.noseparte.game.pack;

import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.pack.entity.ActorBag;

import java.util.List;

public interface BagStrategyHandler {

    void initActorBag(Long rid);

    ErrorCode putItemsAnyway(ActorBag pack, List<HoldItem> getItems);

    ErrorCode putItem(ActorBag pack, HoldItem getItem);

    ActorBag getSpecificBackpack(Long rid);
}
