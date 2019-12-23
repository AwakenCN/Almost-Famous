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
 * @date 2019/9/17 11:58
 * @Description
 */
@Slf4j
public class DeleteCardGroupRequest extends RequestSync {

    private DeleteCardGroupCmd deleteCardGroupCmd;

    public DeleteCardGroupRequest(DeleteCardGroupCmd deleteCardGroupCmd) {
        this.deleteCardGroupCmd = deleteCardGroupCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, deleteCardGroupCmd.toKeyValuePair(), new DeleteCardGroupResposne());
    }

    class DeleteCardGroupResposne implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject jsonObject = getJSONObject(result);
            Integer code = jsonObject.getInteger("code");
            if(code == ErrorCode.SERVER_SUCCESS.value()){
                if (log.isInfoEnabled()){
                    log.info("卡组删除成功");
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
