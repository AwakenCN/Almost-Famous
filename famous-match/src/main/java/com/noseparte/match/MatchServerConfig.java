package com.noseparte.match;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Auther: Noseparte
 * @Date: 2019-12-24 22:36
 * @Description:
 *
 *          <p>match服</p>
 *          <p>系统参数</p>
 */
@Data
@Component
@Order(1)
@ConfigurationProperties(prefix="match")
public class MatchServerConfig {
    private int port;
    private String gameCoreUrl;
    private String adminUrl;
    private int matcher;    // 匹配人数
    private int lifecycle;// 房间定时器生命周期单位分钟
    private int verifyUserLoginCmd;// 验证玩家登录
    private int pullActorCmd;   // 从game服务器拉取角色信息
    private int threadCount;    // 线程池数量
    private int heartBeatTime;// 连接最大空闲时间
    private int matchMaxWaitTime;// 匹配队列中最大等待时间
    private int leisureMatchRange; // 休闲匹配范围
    private int rankMatchRange;// 排位匹配范围
}
