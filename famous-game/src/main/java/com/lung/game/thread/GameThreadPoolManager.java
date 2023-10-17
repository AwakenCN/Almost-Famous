package com.lung.game.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author noseparte
 * @implSpec 游戏业务线程池
 * @since 2023/8/31 - 11:12
 * @version 1.0
 */
@Getter
@Setter
public class GameThreadPoolManager {

    private int coreThreadNum;
    private String threadName;
    private int maxThreadNum;

    private static ThreadPoolExecutor threadPoolExecutor;

    private static AtomicInteger counter = new AtomicInteger();

    public GameThreadPoolManager(int coreThreadNum, String threadName, int maxThreadNum) {
        this.coreThreadNum = coreThreadNum;
        this.threadName = threadName;
        this.maxThreadNum = maxThreadNum;
        ThreadFactory threadFactory = createTreadFactory(threadName);
        threadPoolExecutor = new ThreadPoolExecutor(coreThreadNum, maxThreadNum,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    /**
     * 定制ThreadFactory
     */
    public ThreadFactory createTreadFactory(String threadName) {
        return new ThreadFactoryBuilder()
                .setNameFormat(threadName + counter.getAndIncrement())
                .setDaemon(false)
                .setPriority(5)
                .build();
    }

    public void execute(MessageTask task) {
        threadPoolExecutor.execute(task);
    }
}
