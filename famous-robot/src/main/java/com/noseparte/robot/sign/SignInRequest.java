package com.noseparte.robot.sign;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.protocol.RoleBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.List;

/**
 * @author Noseparte
 * @date 2019/9/17 11:04
 * @Description
 */
@Slf4j
public class SignInRequest extends RequestSync {

    SignInCmd signInCmd;

    public SignInRequest(SignInCmd signInCmd) {
        this.signInCmd = signInCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, signInCmd.toKeyValuePair(), new SignInResponse());
    }

    class SignInResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                String currency = dataObj.getString("currency");
                List<RoleBean> currencyLst = FastJsonUtils.toList(currency, RoleBean.class);
                if (log.isDebugEnabled()) {
                    log.debug("签到成功, {}", currencyLst);
                }
            }
        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }

}
