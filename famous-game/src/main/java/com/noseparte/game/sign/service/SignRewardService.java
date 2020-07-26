package com.noseparte.game.sign.service;

import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.sign.entity.SignReward;

public interface SignRewardService {

    void initSignRewardMgr(Long rid);

    SignReward getSignHistoryByRole(Long rid);

    void updateActorSignHistory(SignReward signReward);

    ErrorCode getSignReward(Long rid, Integer day);
}
