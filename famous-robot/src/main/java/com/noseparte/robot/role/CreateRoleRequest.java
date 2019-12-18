package com.noseparte.robot.role;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class CreateRoleRequest extends RequestSync {

    CreateRoleCmd createRoleCmd;

    public CreateRoleRequest(CreateRoleCmd createRoleCmd) {
        this.createRoleCmd = createRoleCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, createRoleCmd.toKeyValuePair(), new CreateRoleResponse());
    }

    class CreateRoleResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject obj = getJSONObject(result);
            int code = obj.getInteger("code");
            if (code == ErrorCode.ROLE_EXIST.value()) {
                log.error("角色已经存在");
            } else if (code == ErrorCode.SERVER_SUCCESS.value()) {
                try {
                    RoleListCmd roleListCmd = new RoleListCmd();
                    roleListCmd.setCmd(RegisterProtocol.ROLE_LIST_ACTION_REQ);
                    roleListCmd.setUid(createRoleCmd.getUid());
                    roleListCmd.setToken(createRoleCmd.getToken());
                    roleListCmd.setIndex(createRoleCmd.getIndex());
                    new RoleListRequest(roleListCmd).execute();
                } catch (Exception e) {
                    log.error("Get role list: ", e);
                }
            }
        }

        @Override
        public void failed(Exception ex) {
            log.error("", ex);
        }

        @Override
        public void cancelled() {
            log.error("cancelled");
        }
    }

}
