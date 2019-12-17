package com.noseparte.robot.login;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.role.RoleListCmd;
import com.noseparte.robot.role.RoleListRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class LoginRequest extends RequestSync {
    LoginCmd loginCmd;

    public LoginRequest(LoginCmd loginCmd) {
        this.loginCmd = loginCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.adminUrl, loginCmd.toKeyValuePair(), new LoginResponse());
    }

    class LoginResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject obj = getJSONObject(result);
            int code = obj.getInteger("code");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                String data = obj.getString("data");
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                long uid = dataObj.getLong("uid");
                String token = dataObj.getString("token");
                try {
                    RoleListCmd roleListCmd = new RoleListCmd();
                    roleListCmd.setCmd(RegisterProtocol.ROLE_LIST_ACTION_REQ);
                    roleListCmd.setUid(uid);
                    roleListCmd.setToken(token);
                    roleListCmd.setIndex(loginCmd.getIndex());
                    new RoleListRequest(roleListCmd).execute();
                } catch (Exception e) {
                    log.error("Get role list: ", e);
                }
            } else if (code == ErrorCode.ACCOUNT_NOT_EXIST.value()) {
                RegisterCmd registerCmd = new RegisterCmd();
                registerCmd.setCmd(RegisterProtocol.USER_REGISTER_ACTION_REQ);
                registerCmd.setAccount(loginCmd.getAccount());
                registerCmd.setPassword(loginCmd.getPassword());
                try {
                    new RegisterRequest(registerCmd).execute();
                } catch (Exception e) {
                    log.error("Account login: ", e);
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
