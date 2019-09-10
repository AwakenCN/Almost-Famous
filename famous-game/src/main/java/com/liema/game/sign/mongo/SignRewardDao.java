package com.liema.game.sign.mongo;

import com.liema.common.db.dao.GeneralDao;
import com.liema.game.card.entity.Card;
import com.liema.game.sign.entity.SignReward;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


public interface SignRewardDao extends GeneralDao<SignReward> {

    SignReward getSignHistoryByRole(Long rid);

    void insertActorReward(SignReward sign);

    void updateActorSignHistory(SignReward signReward);
}
