package com.noseparte.robot.school;

import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * @author Noseparte
 * @date 2019/9/17 12:16
 * @Description
 */
@Slf4j
public class CardGroupRequest extends RequestSync {

    private CardGroupCmd cardGroupCmd;

    public CardGroupRequest(CardGroupCmd cardGroupCmd) {
        this.cardGroupCmd = cardGroupCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, cardGroupCmd.toKeyValuePair(), new CardGroupResponse());
    }

    class CardGroupResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
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
