package com.noseparte.robot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/22 9:31
 * @Description: 
 * 
 *          <p>robot configuration</p>
 *          <p>配置脚本</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "robot")
public class RobotConfig {

    private int threadCount;
    private String gameCoreUrl;
    private String adminUrl;
    private int robotCount;
    private int robotType;
    private int robotIndex;
    private String accountPre;
    private String robotModel;

}
