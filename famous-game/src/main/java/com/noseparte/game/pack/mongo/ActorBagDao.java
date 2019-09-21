package com.noseparte.game.pack.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.pack.entity.ActorBag;


public interface ActorBagDao extends GeneralDao<ActorBag> {

    void updateActorBag(ActorBag pack);

    void insertActorBag(ActorBag card);

    ActorBag getSpecificBackpack(Long rid);

}
