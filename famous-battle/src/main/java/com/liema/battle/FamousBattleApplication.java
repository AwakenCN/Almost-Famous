package com.liema.battle;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 */
import com.liema.battle.server.BattleServer;
import com.liema.common.thread.ThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 */
@SpringBootApplication
public class FamousBattleApplication {

    @Resource
    BattleServer battleServer;

    public static ThreadPool pool;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousBattleApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

}
