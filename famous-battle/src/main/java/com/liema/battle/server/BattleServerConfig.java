package com.liema.battle.server;

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


}
