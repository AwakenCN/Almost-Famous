package com.noseparte.game.sign.entity;

import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @author noseparte
 * @link github.com/noseparte
 * @date 2020/11/19 - 20:17
 * @implSpec
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "famous-game-sign-reward")
public class SignReward extends GeneralBean {

    /**
     * uid
     */
    private Long rid;

    /**
     * reward records
     */
    private Map<Integer, RewardBean> rewards;

}
