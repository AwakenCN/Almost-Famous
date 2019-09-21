package com.noseparte.battle;

import com.noseparte.battle.match.MatchMgr;
import com.noseparte.battle.server.BattleServer;
import com.noseparte.common.global.ConfigManager;
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

public class BattleServerEventListener implements ApplicationListener {

    private static final Logger LOG = LogManager.getLogger("battle");
    BattleServer battleServer;
    MatchMgr matchMgr;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            LOG.debug("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            LOG.debug("初始化完成");
            LOG.debug("初始GameData策划数据");
            String path = BattleServerEventListener.class.getResource("/gamedata").getFile();
            ConfigManager.loadGameData(path);
        } else if (event instanceof ContextRefreshedEvent) {
            LOG.debug("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            LOG.debug("应用已启动完成");
        } else if (event instanceof ContextStartedEvent) {
            LOG.debug("应用启动，需要在代码动态添加监听器才可捕获");
            ApplicationContext context = ((ContextClosedEvent) event).getApplicationContext();
            battleServer = context.getBean("battleServer", BattleServer.class);
            matchMgr = context.getBean("matchMgr", MatchMgr.class);
            matchMgr.start();
            // 启动网络放到最后
            battleServer.start();
        } else if (event instanceof ContextStoppedEvent) {
            LOG.debug("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            LOG.error("应用关闭");
            battleServer.close();
            matchMgr.close();
        } else {
            LOG.error("What???????????????????? ", event);
        }
    }
}
