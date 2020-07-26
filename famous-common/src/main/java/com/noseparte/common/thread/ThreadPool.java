package com.noseparte.common.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Noseparte
 * @Date: 2019/11/27 10:35
 * @Description:
 *
 *          <p>定制协议网关线程池</p>
 */
public class ThreadPool {

    protected final static Logger _LOG = LogManager.getLogger(ThreadPool.class);
    private List<ExecutorService> workers = new ArrayList<>();
    private int threadCount;
    private ThreadFactory threadFactory;

    public ThreadPool(int threadCount, String poolName) {
        this(threadCount, new UserThreadFactory(poolName));
    }

    public ThreadPool(int threadCount, ThreadFactory threadFactory) {
        this.threadCount = threadCount;
        this.threadFactory = threadFactory;
        if (threadCount <= 0 || null == threadFactory)
            throw new IllegalArgumentException();

        for (int i = 0; i < threadCount; i++) {
            workers.add(new ThreadPoolExecutor(threadCount, 200,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy()));
        }
    }

    public Future execute(Runnable task, int mold) {
        int index = Math.abs(mold) % threadCount;
        ExecutorService executor = workers.get(index);
        if (null == executor) {
            _LOG.error("sid=" + mold + ", tid=" + index);
            return null;
        }
        return executor.submit(task);
    }

    public void shutdown() {
        int count = 0;
        for (ExecutorService worker : workers) {
            _LOG.error("close thread{}.", ++count);
            worker.shutdown();
        }
    }

    static class UserThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        UserThreadFactory(String poolName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = poolName + "-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

    }


}
