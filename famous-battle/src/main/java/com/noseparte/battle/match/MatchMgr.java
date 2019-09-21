package com.noseparte.battle.match;

import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.asynchttp.LoadActorRequest;
import com.noseparte.battle.server.Session;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class MatchMgr implements Runnable {

    public enum MatchEnum {
        CANCEL, DISCONNECT;
    }

    Object nextRunnableLock = new Object();

    private LinkedBlockingQueue<Session> matchQueue = new LinkedBlockingQueue<>();

    @Autowired
    public BattleServerConfig battleServerConfig;

    @Setter
    private volatile boolean stop = false;

    public void addMatchQueue(Session session) {
        synchronized (nextRunnableLock) {
            try {
                if (this.matchQueue.contains(session)) {
                    if (log.isInfoEnabled()) {
                        log.info("repeat match: ", session);
                    }
                    return;
                }
                this.matchQueue.put(session);
            } catch (InterruptedException e) {
                log.error("add match actor error: ", e);
            }
        }
    }

    public void remove(int sid, MatchEnum reason) {
        synchronized (nextRunnableLock) {
            Iterator<Session> iterator = this.matchQueue.iterator();
            while (iterator.hasNext()) {
                Session session = iterator.next();
                if (session.getSid() == sid) {
                    if (log.isDebugEnabled()) {
                        log.debug("从匹配队列中移出玩家role={}，sid={}, reason={}", session.getRoleId(), session.getSid(), reason);
                    }
                    this.matchQueue.remove(session);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        int matcher = battleServerConfig.getMatchers();
        synchronized (nextRunnableLock) {
            while (stop) {
                try {
                    if (matchQueue.size() >= matcher) {
                        List<Session> sessions = new ArrayList<Session>(matcher);
                        for (int i = 0; i < matcher; i++) {
                            Session a = matchQueue.poll();
                            sessions.add(a);
                        }
                        new LoadActorRequest(sessions).execute();
                        continue;
                    }
//                log.debug("Match queue size={}", matchQueue.size());
                    nextRunnableLock.wait(500);
                } catch (Exception e) {
                    log.error("匹配出错{}", e);
                }
            }
        }
    }

    public void start() {
        synchronized (nextRunnableLock) {
            if (this.stop) {
                log.error("重复启动匹配战斗线程");
                return;
            }
            setStop(true);
            Thread t = new Thread(this);
            t.setDaemon(true);
            t.setName("MatchMgr");
            t.start();
            log.error("匹配战斗线程启动");
        }

    }

    public void close() {
        synchronized (nextRunnableLock) {
            if (!this.stop) {
                log.error("已经停止匹配战斗线程.");
                return;
            }
            setStop(false);
            log.error("停止匹配战斗线程....");
        }

    }

}
