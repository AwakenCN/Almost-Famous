package com.noseparte.common.bean;

import lombok.Data;

@Data
public class RewardBean {

    private int day;
    private int status;
    private String drop;
    private Long takeTick;
    private Long signTick;

}
