package com.liema.common.exception;

/**
 * @author Noseparte
 * @date 2019/8/12 17:49
 * @Description
 */
public enum ErrorCode {

    /*成功*/
    SERVER_SUCCESS(0),

    /*客户端参数错误*/
    CLIENT_PARAMS_ERROR(1),

    /*token过期*/
    TOKEN_EXPIRE_ERROR(2),

    /*重名*/
    REPEAT_NAME_ERROR(3),

    /*未知的协议号*/
    UNKNOWN_PROTOCOL(4),

    /*服务器错误*/
    SERVER_ERROR(500),

    /**
     * 账号不存在
     */
    ACCOUNT_NOT_EXIST(1000),

    /**
     * 获取签到信息失败
     */
    SIGN_LIST_ERROR(3004),
    /**
     * 签到失败
     */
    SIGN_ERROR(3005),
    /**
     * 时间未到，不可领取
     */
    REWARD_UNAVAILABLE_TIME(3006);


    ErrorCode(int i) {
        this.value = i;
    }

    int value;

    public int value() {
        return this.value;
    }

}