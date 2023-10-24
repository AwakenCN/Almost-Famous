package com.lung.game.service;

import com.lung.game.params.RankEvent;

public interface RankService {

    void leaderboard(RankEvent rankEvent, RankEvent.RankType rankType, String uid, int score);
}
