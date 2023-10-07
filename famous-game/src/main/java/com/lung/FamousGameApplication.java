package com.lung;

import com.google.common.base.Strings;
import com.google.common.cache.*;
import com.lung.game.entry.UserProfile;
import com.lung.game.params.ConcurrentLock;
import com.lung.game.proto.msg.MsgHandler;
import com.lung.game.thread.GameThreadPoolManager;
import com.lung.server.WebsocketServer;
import com.lung.utils.LockUtils;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Noseparte
 * @Date: 2019/9/27 17:19
 * @Description:
 *
 *          <p>Famous-Game服务 启动类</p>
 */
@ImportAutoConfiguration(RedissonAutoConfiguration.class)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class FamousGameApplication implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(FamousGameApplication.class);

    public static GameThreadPoolManager gameThreadPoolManager = new GameThreadPoolManager(5, "almost-famous_", 20);

    @Autowired
    private WebsocketServer websocketServer;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousGameApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            MsgHandler.getInstance().init("com.lung.game");
            setUserCache();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        websocketServer.start(8888);
    }

    LoadingCache<String, UserProfile> userCache;

    public void setUserCache() {
        userCache = CacheBuilder.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .recordStats()
                .removalListener(addRemoveListener())
                .build(loadUser());
    }

    private RemovalListener<String, UserProfile> addRemoveListener() {
        return notification -> {
            String uid = notification.getKey();
            if (Strings.isNullOrEmpty(uid)) {
                return;
            }
            UserProfile userProfile = notification.getValue();
            if (userProfile != null) {
                logger.info("user profile has removed. uid {}, name {}", notification.getKey(), userProfile.getUid());
                String lockKey = ConcurrentLock.getInstance().getKey(ConcurrentLock.LockType.LOGIN_CONCURRENT_LOCK, uid);
                ReentrantLock lock = LockUtils.getLock(lockKey);
                try {
                    userCache.refresh(uid);
                } catch (Exception e) {
                    userProfile.setDirty(true);
                } finally {
                    lock.unlock();
                    LockUtils.unLock(lockKey);
                }
            }
        };
    }

    private CacheLoader<String, UserProfile> loadUser() {
        return new CacheLoader<String, UserProfile>() {
            @Override
            public UserProfile load(String uid) {
                try {
                    logger.info("load user profile begin {}", uid);
                    UserProfile userProfile = UserProfile.getLoggedUserProfile(uid, false);
                    logger.info("load user profile {}", uid);
                    return userProfile;
                } catch (Exception e) {
                    logger.error("load user profile error, uid = {}", uid, e);
                    return null;
                }
            }
        };
    }


}
