package com.noseparte.common.bean;

public enum StateCode {

    /**
     * 领取未开始
     */
    NOT_STARTED(1),

    /**
     * 进行中
     */
    IN_PROGRESS(2),

    /**
     * 完成
     */
    COMPLETED(3);

    int value;

    StateCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

}
