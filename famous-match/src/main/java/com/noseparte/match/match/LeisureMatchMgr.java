package com.noseparte.match.match;

import com.noseparte.common.battle.BattleModeEnum;
import com.noseparte.common.battle.MatchEnum;
import com.noseparte.common.battle.SimpleActor;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.cache.RedissonUtils;
import com.noseparte.common.global.KeyPrefix;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RScoredSortedSet;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class LeisureMatchMgr extends AbstractMatch {

    String match_leisure_mode_queue_key = KeyPrefix.BattleRedisPrefix.MATCH_LEISURE_MODE_QUEUE;

    Object nextRunnableLock = new Object();
    @Setter
    private volatile boolean stop = false;

    @Override
    public void add(Actor actor) {
        try {
            String match_queue_by_role_key = KeyPrefix.BattleRedisPrefix.MATCH_QUEUE_BY_ROLE + actor.getRid();
            if (RedissonUtils.isExists(redissonClient, match_queue_by_role_key)) {
                log.error("已存在{}，不能重复匹配.", match_queue_by_role_key);
                return;
            }
            RedissonUtils.lock(redissonClient, match_leisure_mode_queue_key);
            RedissonUtils.set(actor, redissonClient, match_queue_by_role_key, matchServerConfig.getMatchMaxWaitTime());
            RScoredSortedSet<SimpleActor> scoredSortedSet = redissonClient.getScoredSortedSet(match_leisure_mode_queue_key);
            int schoolLevel = actor.getSchoolLevel();
            for (int i = 0; i <= matchServerConfig.getLeisureMatchRange(); i++) {
                int minLv = value(schoolLevel, i, false);
                int maxLv = value(schoolLevel, i, true);

                Collection<SimpleActor> simpleActors = scoredSortedSet.valueRange(minLv, true, maxLv, true);
                if (CollectionUtils.isEmpty(simpleActors)) {
                    continue;
                }
                simpleActors.stream()
                        .forEach(simpleActor -> {
                            long roleId = simpleActor.getRoleId();
                            if (roleId == actor.getRid()) {
                                scoredSortedSet.remove(simpleActor);

                            }
                            scoredSortedSet.remove(simpleActor);
                            createSimpleBattleRoom(BattleModeEnum.LEISURE, roleId, actor.getRid());
                        });
                return;
            }
            SimpleActor simpleActor = new SimpleActor(actor.getRid(), actor.getSchoolLevel(), actor.getMatchBeginTime());
            scoredSortedSet.add(actor.getSchoolLevel(), simpleActor);
        } catch (Exception e) {
            log.error("add leisure match error: ", e);
        } finally {
            RedissonUtils.unlock(redissonClient, match_leisure_mode_queue_key);
        }
    }

    @Override
    public void remove(Actor actor, MatchEnum reason) {
        if (actor.getBattleMode() != BattleModeEnum.LEISURE)
            return;
        try {
            RedissonUtils.lock(redissonClient, match_leisure_mode_queue_key);
            RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(match_leisure_mode_queue_key);
            SimpleActor simpleActor = new SimpleActor(actor.getRid(), actor.getSchoolLevel(), actor.getMatchBeginTime());
            scoredSortedSet.remove(simpleActor);
            if (log.isDebugEnabled()) {
                log.debug("从休闲模式匹配队列中移出玩家role={}，reason={}", actor.getRid(), reason);
            }
        } finally {
            RedissonUtils.unlock(redissonClient, match_leisure_mode_queue_key);
        }

    }

    @Override
    public void start() {
        synchronized (nextRunnableLock) {
            if (this.stop) {
                log.error("重复启动休闲模式匹配战斗线程");
                return;
            }
            setStop(true);
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.setName("LeisureMatchMgr");
            t.start();
            log.error("休闲模式匹配战斗线程启动");
        }
    }

    @Override
    public void close() {
        synchronized (nextRunnableLock) {
            if (!this.stop) {
                log.error("已经停止休闲模式匹配战斗线程.");
                return;
            }
            setStop(false);
            log.error("停止休闲模式匹配战斗线程....");
        }
    }

    @Override
    public void run() {
        synchronized (nextRunnableLock) {
            while (stop) {
                try {
                    try {
                        if (!RedissonUtils.lock(redissonClient, match_leisure_mode_queue_key)) {
                            continue;
                        }
                        long now = System.currentTimeMillis();
                        RScoredSortedSet<SimpleActor> scoredSortedSet = redissonClient.getScoredSortedSet(match_leisure_mode_queue_key);
                        Collection<SimpleActor> simpleActors = scoredSortedSet.valueRange(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true);
                        for (SimpleActor simpleActor : simpleActors) {
                            long matchBeginTime = simpleActor.getMatchBeginTime();
                            long remainingTime = (now - matchBeginTime) / 1000;
                            if (matchServerConfig.getMatchMaxWaitTime() > remainingTime)
                                continue;
                            ////////////////////////////////
                            // Do something overtime
                            scoredSortedSet.remove(simpleActor);
                        }
                    }  finally {
                        RedissonUtils.unlock(redissonClient, match_leisure_mode_queue_key);
                    }
                    nextRunnableLock.wait(3000);
                } catch (Exception e) {
                    log.error("休闲匹配出错{}", e);
                }
            }
        }
    }


}
