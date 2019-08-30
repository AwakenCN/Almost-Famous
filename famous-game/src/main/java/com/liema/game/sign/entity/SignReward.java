package com.liema.game.sign.entity;

import com.liema.common.bean.RewardBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SignReward {

    private Long rid;

    private Map<Integer, RewardBean> rewards;

    public SignReward(Long rid, String json) {
        this.rid = rid;
        this.rewards = Misc.parseToMap(json, Integer.class, RewardBean.class);
    }

}
