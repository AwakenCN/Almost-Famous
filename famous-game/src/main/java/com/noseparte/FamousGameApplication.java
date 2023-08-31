package com.noseparte;

import com.noseparte.game.thread.GameThreadPoolManager;
import com.noseparte.server.WebsocketServer;
import jakarta.annotation.Resource;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 17:19
 * @Description:
 *
 *          <p>Famous-Game服务 启动类</p>
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class FamousGameApplication implements CommandLineRunner {

    public static GameThreadPoolManager gameThreadPoolManager = new GameThreadPoolManager(5, "almost-famous_", 20);

    @Autowired
    private WebsocketServer websocketServer;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousGameApplication.class);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        websocketServer.start(8888);
    }

}
