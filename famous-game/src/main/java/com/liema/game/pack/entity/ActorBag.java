package com.liema.game.pack.entity;

import com.liema.common.bean.BagBean;
import com.liema.common.global.Misc;
import com.liema.game.pack.BagStrategyHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Map;

@Data
@NoArgsConstructor
public class ActorBag {

    @Transient
    private BagStrategyHandler bagStrategyHandler;

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
