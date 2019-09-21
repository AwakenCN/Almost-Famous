package com.noseparte.common.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BattleRoomResult {
    List<Long> winners = new ArrayList<>(2);
    List<Long> losers = new ArrayList<>(2);
}
