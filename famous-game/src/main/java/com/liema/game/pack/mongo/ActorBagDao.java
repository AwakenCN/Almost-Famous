package com.liema.game.pack.mongo;

import com.liema.game.pack.entity.ActorBag;

public interface ActorBagDao {

    void updateActorBag(ActorBag pack);

    void insertActorBag(ActorBag card);

    ActorBag getSpecificBackpack(Long rid);
}
