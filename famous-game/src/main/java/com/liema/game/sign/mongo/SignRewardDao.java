package com.liema.game.sign.mongo;

import com.liema.game.sign.entity.SignReward;

public interface SignRewardDao {

    SignReward getSignHistoryByRole(Long rid);

    void insertActorReward(SignReward sign);

    void updateActorSignHistory(SignReward signReward);
}
