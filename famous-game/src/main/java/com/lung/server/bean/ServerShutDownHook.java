package com.lung.server.bean;

import com.lung.game.constans.ServerConstant;
import com.lung.game.persist.redis.RedisSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author noseparte
 * @implSpec 应用程序关闭时的钩子
 * @since 2023/10/12 - 16:23
 * @version 1.0
 */
@Component
public class ServerShutDownHook implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    RedisSession redisSession;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        redisSession.setIntValue(ServerConstant.SERVER_STATE_KEY, ServerState.STOP_PREPARE.ordinal());
        // 在应用程序关闭时执行的逻辑
        System.out.println("Application is shutting down...");
        // 添加你的清理代码或其他处理逻辑
        redisSession.setIntValue(ServerConstant.SERVER_STATE_KEY, ServerState.SHUTDOWN.ordinal());
    }
}
