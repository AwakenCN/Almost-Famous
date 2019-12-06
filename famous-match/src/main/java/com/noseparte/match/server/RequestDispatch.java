package com.noseparte.match.server;

import com.noseparte.common.battle.server.Protocol;
import com.noseparte.match.FamousMatchApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDispatch {

    protected static Logger LOG = LoggerFactory.getLogger("Battle");

    public static void dispatch(Protocol protocol, int sid) {
        try {
            FamousMatchApplication.pool.execute(protocol, sid);
        } catch (Exception e){
            LOG.error("协议执行出错{}", e);
        }
    }

}
