package com.lung.game.params;

import lombok.Getter;

public interface RankEvent {

    int NONE = 0;
    /**
     * 活动
     */
    int ACTIVITY = 1;
    /**
     * 游戏玩法
     */
    int PLAY = 2;


    @Getter
    enum RankType {
        POWER(1),
        ;

        final int type;

        RankType(int type) {
            this.type = type;
        }
    }
}
