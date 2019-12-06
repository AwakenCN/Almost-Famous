package com.noseparte.common.battle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleActor implements Comparable<SimpleActor> {
    long roleId;
    int schoolLevel;
    long matchBeginTime;

    public SimpleActor() {
    }

    @Override
    public int compareTo(SimpleActor o) {
        return 0;
    }
}
