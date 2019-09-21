package com.noseparte.game.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HoldItem {

    /**
     * 掉落类型
     */
    public int code;

    /**
     * 道具数量
     */
    public int num = 1;
    /**
     * 位置索引
     */
    public int index;
    /**
     * 道具基类
     */
    public int itemId;
    /**
     * 品质
     */
    public int quality;
    /**
     * 强化等级
     */
    public int upLevel;
    /**
     * 附加值
     */
    public int value;

    public HoldItem(int itemId, int code) {
        this.itemId = itemId;
        this.code = code;
    }

    public HoldItem(int code, int num, int index, int itemId, int quality, int upLevel, int value) {
        this.code = code;
        this.num = num;
        this.index = index;
        this.itemId = itemId;
        this.quality = quality;
        this.upLevel = upLevel;
        this.value = value;
    }
}
