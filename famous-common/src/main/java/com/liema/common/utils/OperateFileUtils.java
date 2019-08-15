package com.liema.common.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Noseparte
 * @date 2019/8/1 15:19
 * @Description
 */
public class OperateFileUtils {

    public static void main(String[] args) throws InterruptedException {

        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.writeLock().lock();
                System.out.println("Thread real execute");
                lock.writeLock().unlock();
            }
        });

        lock.writeLock().lock();
        lock.writeLock().lock();
        t.start();
        TimeUnit.MICROSECONDS.sleep(200);

        System.out.println("realse one once");
        lock.writeLock().unlock();
    }


}
