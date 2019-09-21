package com.noseparte.common.bean;

public enum AttrCode {

    GOLD(1),
    SILVER(2),
    DIAMOND(3),
    EXP(4),
    CARD(5),
    ITEM(6),//道具
    CARD_PACKAGE(7),
    PACKAGES(8),//礼包
    SELF(9),//掉落组
    LEVEL(10);//等级


    public final int value;

    AttrCode(int i) {
        this.value = i;
    }

    public final int value() {
        return this.value;
    }

    public static AttrCode parase(int value) {
        switch (value) {
            case 1:
                return GOLD;
            case 2:
                return SILVER;
            case 3:
                return DIAMOND;
        }
        return null;
    }
}
