package com.noseparte.game.base.listener;

import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.rpc.GrpcClient;
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
 * @author Noseparte
 * @date 2019/8/21 12:17
 * @Description
 */
public class ApplicationEventListener implements ApplicationListener {

    public final static Logger LOG = LogManager.getLogger("GameCore");

    private GrpcClient grpcClient;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            LOG.debug("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            LOG.debug("初始化完成");
            LOG.debug("初始GameData策划数据");
            String path = ApplicationEventListener.class.getResource("/gamedata").getFile();
            ConfigManager.loadGameData(path);
        } else if (event instanceof ContextRefreshedEvent) {
            LOG.debug("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            LOG.debug("应用已启动完成");
        } else if (event instanceof ContextStartedEvent) {
            LOG.debug("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) {
            LOG.debug("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            ApplicationContext applicationContext = ((ContextClosedEvent) event).getApplicationContext();
            grpcClient = applicationContext.getBean(GrpcClient.class);
            grpcClient.close();
            LOG.debug("应用关闭");
        }
    }

}
