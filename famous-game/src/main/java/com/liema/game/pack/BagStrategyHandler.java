package com.liema.game.pack;

import com.liema.common.exception.ErrorCode;
import com.liema.game.item.HoldItem;
import com.liema.game.pack.entity.ActorBag;

import java.util.List;

public interface BagStrategyHandler {

    void initActorBag(Long rid);

    ErrorCode putItemsAnyway(ActorBag pack, List<HoldItem> getItems);

    ErrorCode putItem(ActorBag pack, HoldItem getItem);

    ActorBag getSpecificBackpack(Long rid);
}
