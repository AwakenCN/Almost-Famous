package com.noseparte.common.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class CardBean implements Serializable {

    int cardId;
    int num;
    int clock;  // default：0 unlocked；
}
