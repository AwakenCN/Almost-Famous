package com.noseparte.robot.mission;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 10:57
 * @Description
 */
@Slf4j
public class FinishRequest extends RequestSync {

    FinishCmd finishCmd;

    public FinishRequest(FinishCmd finishCmd) {
        this.finishCmd = finishCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, finishCmd.toKeyValuePair(), new FinishResponse());
    }

    private class FinishResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String missions = dataObj.getString("missions");
                Map<Integer, MissionBean> missMap = Misc.parseToMap(missions, Integer.class, MissionBean.class);
                if (log.isInfoEnabled()) {
                    log.info("robot {} , 任务列表 {}", rid, missMap);
                }
                ActorListCmd actorListCmd = new ActorListCmd();
                actorListCmd.setRid(rid);
                actorListCmd.setCmd(RegisterProtocol.MISSION_ACTOR_LIST_REQ);
                try {
                    new ActorListRequest(actorListCmd).execute();
                } catch (Exception e) {
                    failed(e);
                }
            }
        }

        @Override
        public void failed(Exception ex) {
            if (log.isErrorEnabled()) {
                log.error("");
            }
        }

        @Override
        public void cancelled() {

        }
    }
}
