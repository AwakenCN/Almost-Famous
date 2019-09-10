package com.liema.game.sign.entity;

import com.liema.common.bean.RewardBean;
import com.liema.common.db.pojo.GeneralBean;
import com.liema.common.global.Misc;
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
