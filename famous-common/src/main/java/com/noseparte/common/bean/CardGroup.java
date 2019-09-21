package com.noseparte.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CardGroup implements Serializable {
    long id;
    String name;
    /**
     * 上阵卡牌
     */
    List<String> useCards = new ArrayList<>();
    /**
     * 待选上阵卡牌
     */
    List<Integer> checkCards = new ArrayList<>();

    /**
     * 敏捷
     */
    int agi;

    /**
     * 智力
     */
    int iq;

    /**
     * 力量
     */
    int str;

    /**
     * 显示顺序
     */
    int index;

    /**
     * 卡组完美，不缺少卡牌
     * 0 不缺少， 1缺少
     */
    int perfection;
}
