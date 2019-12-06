package com.noseparte.match.match;

import LockstepProto.C2SReconnect;
import LockstepProto.NetMessage;
import LockstepProto.S2CReconnect;
import LockstepProto.State;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 连接战斗服务器查询是否战斗未完成断线的情况，如果在战斗中发匹配协议会补发战斗房间信息
 */
@Slf4j
public class CReconnect extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SReconnect req = C2SReconnect.parseFrom(msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }
        long roleId = req.getRoleId();
        S2CReconnect.Builder resp = S2CReconnect.newBuilder();
        RankMatchMgr rankMatchMgr = SpringContextUtils.getBean("rankMatchMgr", RankMatchMgr.class);
        // 验证是否有存在的房间
        if (rankMatchMgr.isHaveBattleRoom(roleId)) {
            resp.setState(State.BROKEN_LINE_VALUE);
            if (log.isDebugEnabled()) {
                log.debug("断线玩家有未结束的战斗房间。role={}", roleId);
            }
        }

        Protocol p = new SReconnect();
        p.setType(NetMessage.S2C_Reconnect_VALUE);
        p.setMsg(resp.build().toByteArray());

        LinkMgr.send(channel, p);
    }
}
