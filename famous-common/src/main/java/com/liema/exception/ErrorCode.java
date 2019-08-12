package com.liema.exception;

/**
 * @author Noseparte
 * @date 2019/8/12 17:49
 * @Description
 */
public enum ErrorCode {

    /*成功*/
    SERVER_SUCCESS(0),

    /*服务器错误*/
    SERVER_ERROR(500);


    ErrorCode(int value) {
        this.value = value;
    }

    int value;

    public int getValue() {
        return value;
    }

}