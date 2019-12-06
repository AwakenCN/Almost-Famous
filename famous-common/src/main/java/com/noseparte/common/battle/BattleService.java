package com.noseparte.common.battle;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BattleService implements Comparable<BattleService> {
    int id;
    String host;
    int port;
    int battleRoomCount;

    @Override
    public int compareTo(BattleService o) {
        return this.battleRoomCount - o.getBattleRoomCount();
    }
}
