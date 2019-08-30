package com.liema.game.sign.service;

import com.liema.common.exception.ErrorCode;
import com.liema.game.sign.entity.SignReward;

public interface SignRewardService {

    void initSignRewardMgr(Long rid);

    SignReward getSignHistoryByRole(Long rid);

    void updateActorSignHistory(SignReward signReward);

    ErrorCode getSignReward(Long rid, Integer day);
}
