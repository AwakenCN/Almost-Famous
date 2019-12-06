package com.noseparte.common.battle;

import com.noseparte.common.bean.Actor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleBattleRoom {
    long roomId;
    int seed;
    int mapId;
    List<Actor> actors;
    long createTime;
    String host;
    int port;
    BattleModeEnum battleMode;
}
