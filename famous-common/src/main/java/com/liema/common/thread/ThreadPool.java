package com.liema.common.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author Noseparte
 * @date 2019/8/23 16:01
 * @Description
 */
public class ThreadPool {

    protected final static Logger _LOG = LogManager.getLogger(ThreadPool.class);
    private List<ExecutorService> workers = new ArrayList<>();
    private int threadCount;



}
