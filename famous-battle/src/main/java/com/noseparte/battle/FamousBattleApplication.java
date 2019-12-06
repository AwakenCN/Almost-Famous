package com.noseparte.battle;

import LockstepProto.NetMessage;
import com.noseparte.battle.battle.*;
import com.noseparte.battle.server.BattleServer;
import com.noseparte.common.battle.BattleService;
import com.noseparte.common.battle.server.CHeartBeat;
import com.noseparte.common.battle.server.RegistryProtocol;
import com.noseparte.common.battle.server.SHeartBeat;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.rpc.RpcClient;
import com.noseparte.common.thread.ThreadPool;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Noseparte
 * @date 2019/7/31 9:58
 * @Description
 *
 *      <title>战斗服 启动</title>
 *      <p>启动匹配线程</p>
 *      <p>matchMgr.start();</p>
 *      <p>启动网络放到最后</p>
 *      <p>nettyServer.start();</p>
 */
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.noseparte.battle", "com.noseparte.common.*"})
public class FamousBattleApplication implements CommandLineRunner {

    @Resource
    BattleServer nettyServer;
    @Resource
    BattleServerConfig battleServerConfig;
    @Resource
    RpcClient rpcClient;
    @Resource
    BattleRoomMgr battleRoomMgr;
    @Resource
    RedissonClient redissonClient;

    public static ThreadPool pool;

    Timer registryServiceTimer = new Timer(true);

    static {
        RegistryProtocol.protocolMap.put(NetMessage.C2S_HeartBeat_VALUE, CHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_HeartBeat_VALUE, SHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_State_VALUE, CState.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_State_VALUE, SState.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_LockStep_VALUE, CLockStep.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_LockStep_VALUE, SLockStep.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_PullFrame_VALUE, CPullFrame.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_PullFrame_VALUE, SPullFrame.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_BattleEnd_VALUE, CBattleEnd.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_BattleEnd_VALUE, SPullFrame.class);
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FamousBattleApplication.class);
//        application.addListeners(new SchedulerConfig());
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @PreDestroy
    public void stop() {
        nettyServer.close();
        pool.shutdown();
        rpcClient.close();
        removeBattleService();
    }

    @Override
    public void run(String... args) throws Exception {
        // init thread pool
        pool = new ThreadPool(battleServerConfig.getThreadCount(), "Battle服网关游戏逻辑协议线程池");
        // 注册对外服务
        registryServiceTimer.scheduleAtFixedRate(new RegistryServiceTask(), 0, 5000);
        // 启动网络放到最后
        nettyServer.start();
    }

    public void removeBattleService() {
        // stop timer
        registryServiceTimer.cancel();

        int serverId = battleServerConfig.getServerId();
        // 移除战斗服务器id
        String battle_server_service_list_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE_LIST;
        RedissonUtils.lock(redissonClient, battle_server_service_list_key);
        RedissonUtils.delete_list(serverId, redissonClient, battle_server_service_list_key);
        RedissonUtils.unlock(redissonClient, battle_server_service_list_key);
        // 移除战斗服务器信息
        String battle_server_service_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE + serverId;
        RedissonUtils.lock(redissonClient, battle_server_service_key);
        RedissonUtils.delete(redissonClient, battle_server_service_key);
        RedissonUtils.unlock(redissonClient, battle_server_service_key);
    }

    class RegistryServiceTask extends TimerTask {
        @Override
        public void run() {
            int battleRoomCount = battleRoomMgr.getBattleRoomCount();
            int serverId = battleServerConfig.getServerId();
            String host = battleServerConfig.getHost();
            int port = battleServerConfig.getPort();
            BattleService battleService = new BattleService();
            battleService.setId(serverId);
            battleService.setHost(host);
            battleService.setPort(port);
            battleService.setBattleRoomCount(battleRoomCount);
            // 注册战斗服务器id
            String battle_server_service_list_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE_LIST;
            if (!RedissonUtils.lock(redissonClient, battle_server_service_list_key)) {
                return;
            }
            if (!RedissonUtils.contains_list(serverId, redissonClient, battle_server_service_list_key)) {
                RedissonUtils.add_list(serverId, redissonClient, battle_server_service_list_key, 360);
            }
            RedissonUtils.unlock(redissonClient, battle_server_service_list_key);
            // 注册战斗服务器信息
            String battle_server_service_key = KeyPrefix.BattleRedisPrefix.BATTLE_SERVER_SERVICE + serverId;
            if (!RedissonUtils.lock(redissonClient, battle_server_service_key)) {
                return;
            }
            RedissonUtils.set(battleService, redissonClient, battle_server_service_key, 10);
            RedissonUtils.unlock(redissonClient, battle_server_service_key);
        }

    }
}
