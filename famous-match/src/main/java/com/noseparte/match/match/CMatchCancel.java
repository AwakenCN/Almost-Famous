package com.noseparte.match.match;

import LockstepProto.C2SMatchCancel;
import com.noseparte.common.battle.MatchEnum;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CMatchCancel extends Protocol {
    @Override
    protected void process() throws Exception {
        C2SMatchCancel req = C2SMatchCancel.parseFrom(msg);
        RankMatchMgr rankMatchMgr = SpringContextUtils.getBean("rankMatchMgr", RankMatchMgr.class);
        rankMatchMgr.update(getSid(), MatchEnum.CANCEL);
    }
}
