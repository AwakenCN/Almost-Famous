package com.lung.game.persist.redis;

import org.redisson.api.RScoredSortedSet;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.lung.game.constans.ServerConstant.REDIS_KEY_SEPARATOR;

/**
 * @author noseparte
 * @implSpec redisson工具类
 * @since 2023/9/23 - 17:51
 * @version 1.0
 */
@Component
public class RedisSession {

    @Autowired
    private RedissonClient redissonClient;

    public void setIntValue(String key, int value) {
        // 获取Redisson的Session对象
        RBucket<Integer> bucket = redissonClient.getBucket(key, IntegerCodec.INSTANCE);
        // 设置String类型的键值对
        bucket.set(value);
    }
    public void setStringValue(String key, String value) {
        // 获取Redisson的Session对象
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        // 设置String类型的键值对
        bucket.set(value);
    }

    public void setStringValueAndExpireTime(String key, String value, int time, TimeUnit unit) {
        // 获取Redisson的Session对象
        RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        // 设置String类型的键值对
        bucket.setAsync(value, time, unit);
    }

    public void delete(String key) {
        // 关闭Redisson的Session
        redissonClient.getBucket(key).delete();
    }

    /**
     * 排行榜分数计入
     * @param redisKey 
     * @param uid
     * @param score
     */
    public void rankAdd(String redisKey, String uid, double score) {
        RScoredSortedSet<String> leaderboard = redissonClient.getScoredSortedSet(redisKey, StringCodec.INSTANCE);

        // 组合分数和当前时间的小数作为 score，添加成员到排行榜
        leaderboard.add(score, redisKey + REDIS_KEY_SEPARATOR + uid);
    }
}
