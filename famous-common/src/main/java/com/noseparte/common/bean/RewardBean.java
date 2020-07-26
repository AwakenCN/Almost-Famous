package com.noseparte.common.bean;

import lombok.Data;

@Data
public class RewardBean {

    private int day;
    private int status;
    private String drop;
    private long takeTick;
    private long signTick;

}
