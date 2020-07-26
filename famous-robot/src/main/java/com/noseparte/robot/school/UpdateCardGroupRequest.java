package com.noseparte.robot.school;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * @author Noseparte
 * @date 2019/9/17 12:14
 * @Description
 */
@Slf4j
public class UpdateCardGroupRequest extends RequestSync {

    UpdateCardGroupCmd updateCardGroupCmd;

    public UpdateCardGroupRequest(UpdateCardGroupCmd updateCardGroupCmd) {
        this.updateCardGroupCmd = updateCardGroupCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, updateCardGroupCmd.toKeyValuePair(), new UpdateCardGroupResponse());
    }

    class UpdateCardGroupResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                if (log.isDebugEnabled()) {
                    log.debug("添加或删除卡组成功");
                }
            } else {
                if (log.isErrorEnabled()) {
                    log.error("添加或删除卡组失败");
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
