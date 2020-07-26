package com.noseparte.robot.bag;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.BagBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.enitty.ActorBag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 12:43
 * @Description
 */
@Slf4j
public class BagListRequest extends RequestSync {

    private BagListCmd bagListCmd;

    public BagListRequest(BagListCmd bagListCmd) {
        this.bagListCmd = bagListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, bagListCmd.toKeyValuePair(), new BagListResponse());
    }

    class BagListResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String packages = dataObj.getString("packages");
                Integer probability = dataObj.getInteger("probability");
                Integer buyCount = dataObj.getInteger("buyCount");
                Integer selectCount = dataObj.getInteger("selectCount");
                Map<Integer, BagBean> bagLst = Misc.parseToMap(packages, Integer.class, BagBean.class);

                Robot robot = SpringContextUtils.getBean("robotMgr", RobotMgr.class).getRobot(rid);
                ActorBag actorBag = new ActorBag(rid, bagLst, probability, buyCount, selectCount);
                robot.setActorBag(actorBag);
                if (log.isDebugEnabled()) {
                    log.debug("rid {}, 背包列表 {}", rid, bagLst);
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
