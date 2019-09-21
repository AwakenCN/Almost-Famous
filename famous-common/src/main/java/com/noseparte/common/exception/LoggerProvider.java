package com.noseparte.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Noseparte
 * @date 2019/8/20 10:27
 * @Description
 */
public class LoggerProvider {

    public static class ExceptionLog {
    }

    public static final Logger exceptionLogger = LoggerFactory.getLogger(ExceptionLog.class);

    public static final void addExceptionLog(String msg, Throwable t) {
        exceptionLogger.error(msg, t);
    }
}
