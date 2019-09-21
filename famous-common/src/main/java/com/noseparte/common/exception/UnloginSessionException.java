package com.noseparte.common.exception;

public class UnloginSessionException extends Exception {


    public UnloginSessionException() {
    }

    public UnloginSessionException(String message) {
        super(message);
    }

    public UnloginSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnloginSessionException(Throwable cause) {
        super(cause);
    }

    public UnloginSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
