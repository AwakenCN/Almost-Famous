package com.noseparte.battle.match;

import LockstepProto.C2SMatch;
import com.noseparte.battle.asynchttp.VerifyUserRequest;
import com.noseparte.battle.server.Protocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CMatch extends Protocol {

    @Override
    protected void process() throws Exception {
        C2SMatch req = C2SMatch.parseFrom(this.msg);
        if (log.isInfoEnabled()) {
            log.info("request protocol sid={}, type={}, msg={}", getSid(), type, req.toString());
        }

        long userId = req.getUserId();
        long roleId = req.getRoleId();
        String token = req.getToken();
        int schoolId = req.getSchoolId();
        long groupId = req.getGroupId();

        //验证登录
        new VerifyUserRequest(userId, roleId, token, channel, schoolId, groupId).execute();
    }


}
