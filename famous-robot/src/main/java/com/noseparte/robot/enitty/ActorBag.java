package com.noseparte.robot.enitty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.noseparte.common.bean.BagBean;
import com.noseparte.common.global.Misc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ActorBag {

    @TableId(type = IdType.INPUT)
    public Long rid;

    public Map<Integer, BagBean> packages;

    //掉落橙卡的概率
    public Integer probability;

    public Integer buyCount;

    public Integer selectCount;

    public ActorBag(Long rid, String json, Integer probability, Integer buyCount, Integer selectCount) {
        this.rid = rid;
        this.packages = Misc.parseToMap(json, Integer.class, BagBean.class);
        this.probability = probability;
        this.buyCount = buyCount;
        this.selectCount = selectCount;
    }

}
