package com.lung.game.service.impl;

import com.lung.game.params.RankEvent;
import com.lung.game.persist.redis.RedisSession;
import com.lung.game.service.RankService;
import com.lung.utils.DateUtils;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.lung.game.constans.ServerConstant.REDIS_KEY_SEPARATOR;

@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RedisSession redisSession;

    @Override
    public void leaderboard(RankEvent rankEvent, RankEvent.RankType rankType, String uid, int score) {
        double rankScore = getRankScore(score);
        String redisKey = rankEvent + REDIS_KEY_SEPARATOR + rankType.getType();
        redisSession.rankAdd(redisKey, uid, rankScore);
    }

    public static double getRankScore(int value) {
        if (value == 0) {
            return 0;
        }

        long recordTime = System.currentTimeMillis();
        long endTime = DateUtils.parseFullTimeStr("2099-01-01 00:00:00");

        long diff = endTime - recordTime;

        diff = diff / DateUtils.MINUTEMILLS;
        int length = String.valueOf(diff).length() + 1;
        //
        String s = 1 + String.format("%" + (length - 1) + "s", "0").replace(" ", "0");
        String timeValue = String.format("%." + (length - 1) + "f", diff / Float.parseFloat(s));
        //
        return value + Double.parseDouble(timeValue);
    }
    public static double getRankValue(int value) {
        if (value == 0) {
            return 0;
        }

        return Double.parseDouble(value + "." + (Long.MAX_VALUE - System.currentTimeMillis()) % 1000000000L);
    }

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(getRankScore(100));
//        Thread.sleep(3000);
//        System.out.println(getRankScore(200));
//        Thread.sleep(1000);
//        System.out.println(getRankScore(100));
        System.out.println(getRankValue(100));
        System.out.println(getRankValue(200));
        System.out.println(getRankValue(100));
    }
}
