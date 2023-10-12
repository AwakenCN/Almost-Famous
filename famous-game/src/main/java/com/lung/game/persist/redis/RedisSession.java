package com.lung.game.persist.redis;

import org.redisson.client.codec.Codec;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.StringCodec;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.stereotype.Component;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author haoyitao
 * @implSpec
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
}
