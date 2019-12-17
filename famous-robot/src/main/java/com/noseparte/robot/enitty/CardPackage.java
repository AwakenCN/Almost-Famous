package com.noseparte.robot.enitty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 卡包
 * </p>
 *
 * @author liang
 * @since 2019-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class CardPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    public CardPackage(Long rid, String json, Integer selectCnt, Integer buyCnt) {
        this.rid = rid;
        this.cards = Misc.parseToMap(json, Integer.class, CardBean.class);
        this.selectCnt = selectCnt;
        this.buyCnt = buyCnt;
    }

    @TableId(type = IdType.INPUT)
    private Long rid;

    private Map<Integer, CardBean> cards;

    // 抽卡
    private int selectCnt;

    // 买卡
    private int buyCnt;

}
