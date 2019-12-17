package com.noseparte.robot;

import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.rpc.RpcClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/16 12:32
 * @Description: <p></p>
 */
public class ApplicationEventListener implements ApplicationListener {

    private static final Logger LOG = LogManager.getLogger("Robot");

    private RpcClient rpcClient;

    String configPath;

    public ApplicationEventListener(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            LOG.debug("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            LOG.debug("初始化完成");
            LOG.debug("初始GameData策划数据");
            ConfigManager.loadGameData(this.configPath);
        } else if (event instanceof ContextRefreshedEvent) {
            LOG.debug("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            LOG.debug("应用已启动完成");
        } else if (event instanceof ContextStartedEvent) {
            LOG.debug("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) {
            LOG.debug("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            ApplicationContext context = ((ContextClosedEvent) event).getApplicationContext();
            rpcClient = context.getBean(RpcClient.class);
            rpcClient.close();
            LOG.error("应用关闭");
        } else {

        }
    }
}
