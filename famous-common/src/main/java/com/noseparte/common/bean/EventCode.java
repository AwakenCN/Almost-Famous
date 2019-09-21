package com.noseparte.common.bean;

public enum EventCode {

    GM_ADD_MONEY(10000),
    CARD_BUY(10001),
    CARD_SALE(10002),
    SIGN_ADD_MONEY(10003);

    int value;

    EventCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }
}
