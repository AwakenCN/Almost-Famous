package com.lung.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author haoyitao
 * @implSpec 
 * @since 2023/9/4 - 12:01
 * @version 1.0
 */
public class LockUtils {

    private final static  Logger logger = LoggerFactory.getLogger(LockUtils.class);


    private static Map<String, ReentrantLock> lockMap = new HashMap<>();


    public static ReentrantLock getLock(String lockKey) {
        ReentrantLock reentrantLock;
        synchronized (lockMap) {
            if (lockMap.containsKey(lockKey)) {
                reentrantLock = lockMap.get(lockKey);
            } else {
                reentrantLock = new ReentrantLock();
                lockMap.put(lockKey, reentrantLock);
                return reentrantLock;
            }
        }
        return reentrantLock;
    }

    public static boolean isLock(String lockKey) {
        synchronized (lockMap) {
            return lockMap.containsKey(lockKey);
        }
    }

    public static void unLock(String lockKey) {
        synchronized (lockMap) {
            if (lockMap.containsKey(lockKey)) {
                ReentrantLock lock = lockMap.get(lockKey);
                if (!lock.isLocked()) {
                    lockMap.remove(lockKey);
                }
            }
        }
    }

}
