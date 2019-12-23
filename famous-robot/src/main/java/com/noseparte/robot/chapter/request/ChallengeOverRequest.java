package com.noseparte.robot.chapter.request;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.PropBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.chapter.cmd.ChallengeOverCmd;
import com.noseparte.robot.chapter.cmd.ProgressCmd;
import com.noseparte.robot.school.SchoolListCmd;
import com.noseparte.robot.school.SchoolListRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.List;

/**
 * @author Noseparte
 * @date 2019/9/17 10:19
 * @Description
 */
@Slf4j
public class ChallengeOverRequest extends RequestSync {

    private ChallengeOverCmd challengeOverCmd;

    public ChallengeOverRequest(ChallengeOverCmd challengeOverCmd) {
        this.challengeOverCmd = challengeOverCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, challengeOverCmd.toKeyValuePair(), new ChallengeOverResponse());
    }

    class ChallengeOverResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                String drop = dataObj.getString("drop");
                List<PropBean> propList = FastJsonUtils.toList(drop, PropBean.class);
                if (log.isDebugEnabled()) {
                    log.debug("通关后的奖励信息, {}", propList);
                }
                // 通关后再次调用关卡进度列表 刷新jvm中内存的值
                ProgressCmd progressCmd = new ProgressCmd();
                progressCmd.setCmd(RegisterProtocol.CHAPTER_PROGRESS_REQ);
                progressCmd.setRid(challengeOverCmd.getRid());

                SchoolListCmd schoolListCmd = new SchoolListCmd();
                schoolListCmd.setCmd(RegisterProtocol.SCHOOL_LIST_REQ);
                schoolListCmd.setRid(challengeOverCmd.getRid());
                try {
                    new ProgressRequest(progressCmd).execute();
                    new SchoolListRequest(schoolListCmd).execute();
                } catch (Exception e) {
                    log.error("获取玩家章节列表失败: ", e);
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
