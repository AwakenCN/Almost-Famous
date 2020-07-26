package com.noseparte.robot.role;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class GetRoleAttrRequest extends RequestSync {

    GetRoleAttrCmd getRoleAttrCmd;

    public GetRoleAttrRequest(GetRoleAttrCmd getRoleAttrCmd) {
        this.getRoleAttrCmd = getRoleAttrCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, getRoleAttrCmd.toKeyValuePair(), new GetRoleAttrResponse());
    }

    class GetRoleAttrResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject obj = getJSONObject(result);
            int code = obj.getInteger("code");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                String data = obj.getString("data");
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                long rid = dataObj.getLong("rid");
                int attrId = dataObj.getInteger("attrId");
                long attrVal = dataObj.getLong("attrVal");
                log.debug("rid={}, attrId={}, attrVal={}", rid, attrId, attrVal);
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
