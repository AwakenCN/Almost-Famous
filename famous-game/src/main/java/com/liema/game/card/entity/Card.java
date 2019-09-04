package com.liema.game.card.entity;

import com.liema.common.bean.CardBean;
import com.liema.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 卡牌
 * </p>
 *
 * @author liang
 * @since 2019-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    public Card(Long rid, String json, Integer selectCnt, Integer buyCnt) {
        this.rid = rid;
        this.cards = Misc.parseToMap(json, Integer.class, CardBean.class);
        this.selectCnt = selectCnt;
        this.buyCnt = buyCnt;
    }

    private Long rid;

    private Map<Integer, CardBean> cards;

    // 抽卡
    private int selectCnt;

    // 买卡
    private int buyCnt;

}
