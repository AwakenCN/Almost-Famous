package com.noseparte.robot.bag;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * @author Noseparte
 * @date 2019/9/17 12:32
 * @Description
 */
@Slf4j
public class BagBuyRequest extends RequestSync {

    private BagBuyCmd bagBuyCmd;

    public BagBuyRequest(BagBuyCmd bagBuyCmd) {
        this.bagBuyCmd = bagBuyCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, bagBuyCmd.toKeyValuePair(), new BagBuyResponse());
    }

    class BagBuyResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);

        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }


}
