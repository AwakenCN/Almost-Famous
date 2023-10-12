package com.lung.server.bean;

/**
 * @author haoyitao
 * @implSpec 服务器状态
 * @since 2023/10/12 - 15:38
 * @version 1.0
 */
public enum ServerState {

    /**
     * 准备起服
     */
    START_PREPARE,
    /**
     * 运行中
     */
    STARTING,
    /**
     * 准备停服
     */
    STOP_PREPARE,
    /**
     * 停止运行
     */
    SHUTDOWN

}
