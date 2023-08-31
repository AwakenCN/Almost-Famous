package com.noseparte.game.thread;

import java.util.concurrent.*;

/**
 * @author haoyitao
 * @implSpec 游戏业务线程池
 * @since 2023/8/31 - 11:12
 * @version 1.0
 */
public class GameThreadPoolManager {

    private int coreThreadNum;
    private String threadName;
    private int maxThreadNum;
    private ThreadFactory threadFactory;

    public GameThreadPoolManager(int coreThreadNum, String threadName, int maxThreadNum, ThreadFactory threadFactory) {
        this.coreThreadNum = coreThreadNum;
        this.threadName = threadName;
        this.maxThreadNum = maxThreadNum;
        this.threadFactory = threadFactory;
        new ThreadPoolExecutor(coreThreadNum, maxThreadNum,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
    }

    /**
     * 定制ThreadFactory
     */
    public void createTreadFactory() {

    }


}
