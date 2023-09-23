package com.lung.game.persist.redis;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * @author haoyitao
 * @implSpec
 * @since 2023/9/23 - 17:51
 * @version 1.0
 */
@Service
public class RedisSession {

    private final RedissonClient redissonClient;

    public RedisSession(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
