package com.noseparte.common.exception;

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
     * 角色已存在
     */
    ROLE_EXIST(1001),
    /**
     * 职业不存在
     */
    SCHOOL_NOT_EXIST(1002),
    /**
     * 卡组名称不合法
     */
    CARD_GROUP_NAME_ERROR(1003),
    /**
     * 卡组id不合法
     */
    CARD_GROUP_ID_ERROR(1004),
    /**
     * 卡组id不存在
     */
    CARD_GROUP_ID_NOT_EXIST(1005),
    /**
     * 卡组拼装异常
     */
    CARD_GROUP_ASSEMBLE_ERROR(1006),
    /**
     * 卡组名称不能为空
     */
    CARD_GROUP_NAME_NOT_EXIST(1007),
    /**
     * 卡组不存在
     */
    CARD_GROUP_NOT_EXIST(1008),
    /**
     * 卡组不可用
     */
    CARD_GROUP_NOT_USEFULLY(1009),
    /**
     * 余额不足 gold
     */
    INSUFFICIENT_GOLD(1010),
    /**
     * 余额不足 silver
     */
    INSUFFICIENT_SILVER(1011),
    /**
     * 余额不足 diamond
     */
    INSUFFICIENT_DIAMOND(1012),
    /**
     * 卡组数超过上线
     */
    CARD_GROUP_SIZE_OVERFLOW(1013),

    ///////////////////////////////////////关卡 或 副本
    /**
     * 关卡id不存在
     */
    CHAPTER_ID_NOT_EXIST(2001),

    /**
     * 不能重复刷关卡或副本
     */
    CHAPTER_NOT_REPEAT_CHALLENG(2002),

    /**
     * 关卡或副本战斗时间不足
     */
    CHAPTER_BATTLE_TIME_SHORT(2003),

    ///////////////////////////////////////任务 或 签到
    /**
     * 获取任务配置异常
     */
    MISSION_NOT_EXIST(3001),
    /**
     * 任务奖励已领取
     */
    MISSION_REWARD_RECEIVED(3002),
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
    REWARD_UNAVAILABLE_TIME(3006),

    /**
     * 功能暂未开放，敬请期待
     */
    COMING_SOON(3007);


    ErrorCode(int i) {
        this.value = i;
    }

    int value;

    public int value() {
        return this.value;
    }

}