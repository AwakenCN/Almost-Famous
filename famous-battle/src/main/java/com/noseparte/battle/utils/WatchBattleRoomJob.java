package com.noseparte.battle.utils;

import LockstepProto.Frame;
import LockstepProto.NetMessage;
import LockstepProto.S2CLockStep;
import com.google.protobuf.ByteString;
import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.battle.BattleRoom;
import com.noseparte.battle.battle.BattleRoomMgr;
import com.noseparte.battle.battle.SLockStep;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.bean.BattleRoomResult;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WatchBattleRoomJob implements Job {

    public static final String NUM_EXECUTIONS = "NumExecutions";
    Object nextRunnableLock = new Object();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        int redundancy = battleServerConfig.getRedundancy();
        int intervalInMilliseconds = 1000 / battleServerConfig.getFrameSpeed();
        int executeCount = battleServerConfig.getLifecycle() * 60 * 1000 / intervalInMilliseconds;

        Trigger trigger = context.getTrigger();
        JobDataMap jobDataMap = trigger.getJobDataMap();
        TriggerKey key = trigger.getKey();
        long startTime = (long) jobDataMap.get("startTime");
        //在这里实现业务逻辑
        if (log.isDebugEnabled()) {
            log.debug("房间定时器{}，{}执行.", key.getName(), key.getGroup());
        }
        long roomId = (long) jobDataMap.get("roomId");
        BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
        BattleRoom battleRoom = battleMgr.getBattleRoomById(roomId);

        JobUtil jobUtil = SpringContextUtils.getBean("jobUtil", JobUtil.class);

        while (executeCount > 0) {
            synchronized (nextRunnableLock) {
                try {
                    // 广播帧
                    broadcastFrame(redundancy, key, battleRoom);
                    jobUtil.pauseJob(key.getName(), key.getGroup());
                    nextRunnableLock.wait(intervalInMilliseconds);
                    jobUtil.resumeJob(key.getName(), key.getGroup());
                    // 是否结束
                    long now = System.currentTimeMillis();
                    if (!battleMgr.checkBattleResult(battleRoom, now)) {
                        log.info("BattleRoom={}, game over.", roomId);
                        break;
                    }
                } catch (InterruptedException | SchedulerException e) {
                    log.error("", e);
                }
                executeCount--;
            }
        }
        // 战斗房间结束
        BattleRoomResult battleRoomResult = battleRoom.getBattleRoomResult();
        if (battleRoomResult.getWinners().size() == 0
                && battleRoomResult.getLosers().size() == 0) {
            if (log.isInfoEnabled()) {
                log.info("(5)---时间到了没有收到A和B的战斗结果,都判定输.");
            }
            // 时间到了没有结果，就都判定输
            for (Actor actor : battleRoom.getActors()) {
                battleRoomResult.getLosers().add(actor.getRid());
            }
        }
        // 解散房间
        battleMgr.roomOver(battleRoom);

    }


    private void broadcastFrame(int redundancy, TriggerKey key, BattleRoom battleRoom) {
        // 帧
        int influenceFrameCount = battleRoom.nextFrameNumber();
        Frame.Builder frame = Frame.newBuilder();
        frame.setInfluenceFrameCount(influenceFrameCount);
        for (byte[] f : battleRoom.pollFrame()) {
            frame.addCommands(ByteString.copyFrom(f));
        }
        frame.build();
        // protocol
        S2CLockStep.Builder s2CLockStep = S2CLockStep.newBuilder();
        s2CLockStep.addF(frame);
        for (influenceFrameCount -= 1; 0 < influenceFrameCount && 0 < redundancy--; influenceFrameCount--) {
            s2CLockStep.addF(battleRoom.pullStoreFrame(influenceFrameCount));
        }
        byte[] msg = s2CLockStep.build().toByteArray();
        //store frame
        battleRoom.storeFrame(frame);
        // 广播
        List<Actor> actors = battleRoom.getActors();
        for (Actor actor : actors) {
            Session session = LinkMgr.getSession(actor.getRid());
            if (null == session || !session.getChannel().isActive()) {
                continue;
            }
            Protocol p = new SLockStep();
            p.setType(NetMessage.S2C_LockStep_VALUE);
            p.setMsg(msg);

            session.getChannel().writeAndFlush(p);
        }
    }

}
