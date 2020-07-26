package com.noseparte.robot.mission;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.bean.StateCode;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.enitty.Mission;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 10:54
 * @Description
 */
@Slf4j
public class ActorListRequest extends RequestSync {

    private ActorListCmd actorListCmd;

    public ActorListRequest(ActorListCmd actorListCmd) {
        this.actorListCmd = actorListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, actorListCmd.toKeyValuePair(), new ActorListResponse());
    }

    class ActorListResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String missions = dataObj.getString("missions");
                Map<Integer, MissionBean> missionLst = Misc.parseToMap(missions, Integer.class, MissionBean.class);
                if (log.isDebugEnabled()) {
                    log.debug("rid {}, 任务列表 {}", rid, missionLst);
                }

                RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
                Robot robot = robotMgr.getRobot(rid);
                Mission mission = new Mission();
                mission.setRid(rid);
                mission.setMissions(missionLst);
                robot.setMission(mission);
                for (MissionBean miss : missionLst.values()) {
                    if (miss.getStatus() == StateCode.IN_PROGRESS.value()
                            && miss.getIsShow() == 1) {
                        // 任务列表领取奖励
                        FinishCmd finishCmd = new FinishCmd();
                        finishCmd.setMissionId(miss.getMissionId());
                        finishCmd.setRid(rid);
                        finishCmd.setCmd(RegisterProtocol.MISSION_FINISH_ACTION_REQ);
                        try {
                            new FinishRequest(finishCmd).execute();
                        } catch (Exception e) {
                            if (log.isErrorEnabled()) {
                                log.error("rid, {}, 任务{{}}奖励领取失败", rid, miss.getMissionId());
                            }
                        }
                    }
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
