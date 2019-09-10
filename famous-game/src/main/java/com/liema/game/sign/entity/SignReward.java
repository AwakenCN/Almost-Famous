package com.liema.game.sign.entity;

import com.liema.common.bean.RewardBean;
import com.liema.common.db.pojo.GeneralBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SignReward extends GeneralBean {

    private Long rid;

    private Map<Integer, RewardBean> rewards;

}
