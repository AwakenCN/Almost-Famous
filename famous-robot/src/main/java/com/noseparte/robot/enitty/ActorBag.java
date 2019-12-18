package com.noseparte.robot.enitty;

import com.noseparte.common.bean.BagBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "famous-game-bag")
public class ActorBag extends GeneralBean {

    public Long rid;

    public Map<Integer, BagBean> packages;

    //掉落橙卡的概率
    public Integer probability;

    public Integer buyCount;

    public Integer selectCount;

    public ActorBag(Long rid, Map<Integer, BagBean> packages, Integer probability, Integer buyCount, Integer selectCount) {
        this.rid = rid;
        this.packages = packages;
        this.probability = probability;
        this.buyCount = buyCount;
        this.selectCount = selectCount;
    }
}
