package com.noseparte.robot.enitty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.global.Misc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SignReward {

    @TableId(type = IdType.INPUT)
    private Long rid;

    private Map<Integer, RewardBean> rewards;

    public SignReward(Long rid, String json) {
        this.rid = rid;
        this.rewards = Misc.parseToMap(json, Integer.class, RewardBean.class);
    }

}
