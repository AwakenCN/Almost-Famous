package com.noseparte.common.cache;

import com.noseparte.common.global.KeyPrefix;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedissonUtils {

    public static boolean lock(RedissonClient redissonClient, String key) {
        String lockKey = KeyPrefix.REDISSON_LOCK + key;
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            if (lock.tryLock(200, 1000, TimeUnit.MILLISECONDS)) {
//                if (log.isDebugEnabled()) {
//                    log.debug("lock key={} success", lockKey);
//                }
                return true;
            }
        } catch (InterruptedException e) {
            log.error("", e);
        }
        log.error("lock key={} fail.", lockKey);
        return false;
    }

    public static boolean unlock(RedissonClient redissonClient, String key) {
        String lockKey = KeyPrefix.REDISSON_LOCK + key;
        RLock lock = redissonClient.getFairLock(lockKey);
        if (lock.isLocked()) {
//            if (log.isDebugEnabled()) {
//                log.debug("unlock key={} success", lockKey);
//            }
            lock.unlock();
        }
        return true;
    }

    public static boolean isExists(RedissonClient redissonClient, String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    public static void set(Object o, RedissonClient redissonClient, String key, long expireTime) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (expireTime > 0) {
            bucket.set(o, expireTime, TimeUnit.SECONDS);
        } else {
            bucket.set(o);
        }
    }

    public static <T> T get(RedissonClient redissonClient, String key, Class<T> clazz) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        if (null != bucket) {
            return bucket.get();
        }
        return null;
    }

    public static <T> T getAndDelete(RedissonClient redissonClient, String key, Class<T> clazz) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        if (null != bucket) {
            return bucket.getAndDelete();
        }
        return null;
    }

    public static boolean delete(RedissonClient redissonClient, String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (null != bucket) {
            return bucket.delete();
        }
        return false;
    }

    public static void put_map(Object key, Object value, RedissonClient redissonClient, String mapKey, long expireTime) {
        RMap<Object, Object> map = redissonClient.getMap(mapKey);
        if (expireTime > 0) {
            map.expire(expireTime, TimeUnit.SECONDS);
        }
        map.put(key, value);
    }

    public static void add_list(Object value, RedissonClient redissonClient, String key, long expireTime) {
        RList<Object> list = redissonClient.getList(key);
        if (expireTime > 0) {
            list.expire(expireTime, TimeUnit.SECONDS);
        }
        list.add(value);
    }

    public static boolean contains_list(Object value, RedissonClient redissonClient, String key) {
        RList<Object> list = redissonClient.getList(key);
        return list.contains(value);
    }

    public static void delete_list(Object value, RedissonClient redissonClient, String key) {
        RList<Object> list = redissonClient.getList(key);
        list.remove(value);
    }

    public static boolean multiLock(RedissonClient redissonClient, String... keys) {
        if (null == keys || keys.length == 0) {
            return false;
        }
        RLock[] locks = new RLock[keys.length];
        for (int i = 0; i < keys.length; i++) {
            String lockKey = KeyPrefix.REDISSON_LOCK + keys[i];
            locks[i] = redissonClient.getLock(lockKey);
        }
        RLock multiLock = redissonClient.getMultiLock(locks);
        try {
            if (multiLock.tryLock(200, 1000, TimeUnit.MILLISECONDS)) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unMultiLock(RedissonClient redissonClient, String... keys) {
        if (null == keys || keys.length == 0) {
            return false;
        }
        RLock[] locks = new RLock[keys.length];
        for (int i = 0; i < keys.length; i++) {
            String lockKey = KeyPrefix.REDISSON_LOCK + keys[i];
            locks[i] = redissonClient.getLock(lockKey);
        }
        RLock multiLock = redissonClient.getMultiLock(locks);
        if (multiLock.isLocked()) {
            multiLock.unlock();
        }
        return true;
    }

}
