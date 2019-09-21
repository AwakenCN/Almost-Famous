package com.noseparte.common.bean;

public enum DropCode {
    NULL(0),
    CURRENCY(1),
    EXP(4),
    CARD(5),
    ITEM(6),
    OTHER(8),
    SELF(9);// ������еĵ���id

    int value;

    DropCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DropCode parase(int value) {
        switch (value) {
            case 0:
                return NULL;
            case 1:
                return CURRENCY;
            case 4:
                return EXP;
            case 5:
                return CARD;
            case 6:
                return ITEM;
            case 9:
                return SELF;
        }
        return null;
    }
}
