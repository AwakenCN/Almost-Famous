package com.noseparte.game.sign.entity;

import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "famous-game-sign-reward")
public class SignReward extends GeneralBean {

    private Long rid;

    private Map<Integer, RewardBean> rewards;

}
