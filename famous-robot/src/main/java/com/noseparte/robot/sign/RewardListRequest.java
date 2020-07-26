package com.noseparte.robot.sign;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.RewardBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.enitty.SignReward;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 11:02
 * @Description
 */
@Slf4j
public class RewardListRequest extends RequestSync {

    RewardListCmd rewardListCmd;

    public RewardListRequest(RewardListCmd rewardListCmd) {
        this.rewardListCmd = rewardListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, rewardListCmd.toKeyValuePair(), new RewardListResponse());
    }

    class RewardListResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            if (code == ErrorCode.SIGN_LIST_ERROR.value()) {
                if (log.isErrorEnabled()) {
                    log.error("获取签到信息失败");
                }
            } else if (code == ErrorCode.SERVER_SUCCESS.value()) {
                String data = object.getString("data");
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String rewards = dataObj.getString("rewards");
                Map<Integer, RewardBean> rewardList = Misc.parseToMap(rewards, Integer.class, RewardBean.class);

                Robot robot = SpringContextUtils.getBean("robotMgr", RobotMgr.class).getRobot(rid);
                SignReward signReward = new SignReward(rid, rewardList);
                robot.setSignReward(signReward);
                if (log.isDebugEnabled()) {
                    log.debug("rid {}, 签到记录 {}", rid, rewardList);
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
