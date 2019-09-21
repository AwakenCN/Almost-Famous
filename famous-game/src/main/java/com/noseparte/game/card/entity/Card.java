package com.noseparte.game.card.entity;

import com.noseparte.common.bean.CardBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "famous-game-card")
public class Card extends GeneralBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Card(Long rid, Map<Integer, CardBean> cards, int buyCnt) {
        this.rid = rid;
        this.cards = cards;
        this.buyCnt = buyCnt;
    }

    private Long rid;

    private Map<Integer, CardBean> cards;

    // 买卡
    private int buyCnt;

}
