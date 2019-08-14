package com.liema.exception;

/**
 * @author Noseparte
 * @date 2019/8/12 17:49
 * @Description
 */
public enum ErrorCode {

    /*成功*/
    SERVER_SUCCESS(0),

    /*未知的协议号*/
    UNKNOWN_PROTOCOL(4),

    /*服务器错误*/
    SERVER_ERROR(500);


    ErrorCode(int i) {
        this.value = i;
    }

    int value;

    public int value() {
        return this.value;
    }

}