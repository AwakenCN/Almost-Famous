package com.noseparte.robot.login;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class RegisterRequest extends RequestSync {
    RegisterCmd registerCmd;

    public RegisterRequest(RegisterCmd registerCmd) {
        this.registerCmd = registerCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.adminUrl, registerCmd.toKeyValuePair(), new RegisterResponse());
    }

    class RegisterResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            JSONObject obj = getJSONObject(result);
            int code = obj.getInteger("code");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                LoginCmd loginCmd = new LoginCmd();
                loginCmd.setCmd(RegisterProtocol.USER_LOGIN_ACTION_REQ);
                loginCmd.setAccount(registerCmd.getAccount());
                loginCmd.setPassword(registerCmd.getPassword());
                try {
                    new LoginRequest(loginCmd).execute();
                } catch (Exception e) {
                    log.error("Register account: ", e);
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
