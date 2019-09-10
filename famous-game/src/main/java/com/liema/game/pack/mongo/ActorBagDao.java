package com.liema.game.pack.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.game.pack.entity.ActorBag;


public interface ActorBagDao extends GeneralDao<ActorBag> {

    void updateActorBag(ActorBag pack);

    void insertActorBag(ActorBag card);

    ActorBag getSpecificBackpack(Long rid);

}
