package com.noseparte.battle;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="battle")
public class BattleServerConfig {
    private int serverId;
    private String host;
    private int port;
    private String gameCoreUrl;
    private int matcher;    // 匹配人数
    private int frameSpeed;// 每秒钟帧同步速度
    private int lifecycle;// 房间定时器生命周期单位分钟
    private int redundancy;// 冗余帧数

    private int battleStartCmd; // 通知gamecore战斗开始
    private int battleEndCmd;   // 通知gamecore战斗结束
    private int threadCount;    // 线程池数量
    private int otherActorBattleResult;// 等待其它玩家上传战报最大等待时间
    private int heartBeatTime;// 连接最大空闲时间
}
