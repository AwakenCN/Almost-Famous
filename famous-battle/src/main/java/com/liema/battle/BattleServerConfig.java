package com.liema.battle;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Noseparte
 * @date 2019/8/21 17:40
 * @Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "battle")
public class BattleServerConfig {

    private int port;
    private String gameUrl;
    private String loginUrl;
    private int matchers;  //匹配人数
    private int frameSpeed; //每秒钟帧同步速度
    private int lifecycle;// 房间定时器生命周期单位分钟
    private int redundancy; //冗余帧数
    private int verifyUserLoginCmd; //验证玩家登录
    private int pullActorCmd; //从game服务器拉取角色信息
    private int battleStartCmd; //通知game战斗开始
    private int battleEndCmd; //通知game战斗结束
    private int threadCount; //线程池数量
    private int otherActorBattleResult; //等待其他玩家上传战报最大等待时间
    private int heartBeatTime; //连接最大空闲时间


}
