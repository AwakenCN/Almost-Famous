package com.noseparte.game.sign.mongo;

import com.noseparte.common.db.dao.GeneralDao;
import com.noseparte.game.sign.entity.SignReward;


public interface SignRewardDao extends GeneralDao<SignReward> {

    SignReward getSignHistoryByRole(Long rid);

    void insertActorReward(SignReward sign);

    void updateActorSignHistory(SignReward signReward);
}
