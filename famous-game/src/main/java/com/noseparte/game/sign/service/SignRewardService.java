package com.noseparte.game.sign.service;

import com.noseparte.common.exception.ErrorCode;
import com.noseparte.game.sign.entity.SignReward;

public interface SignRewardService {

    void initSignRewardMgr(Long rid);

    SignReward getSign(Long rid);

    ErrorCode getSignReward(Long rid, Integer day);
}
