package com.noseparte.robot.chapter.request;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.chapter.cmd.ChallengeCmd;
import com.noseparte.robot.chapter.cmd.ChallengeOverCmd;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * @author Noseparte
 * @date 2019/9/17 9:52
 * @Description
 */
@Slf4j
public class ChallengeRequest extends RequestAsync {

    private ChallengeCmd challengeCmd;

    public ChallengeRequest(ChallengeCmd challengeCmd) {
        this.challengeCmd = challengeCmd;
    }

    @Override
    public void execute() throws Exception {
        async(FamousRobotApplication.gameCoreUrl, challengeCmd.toKeyValuePair(), new ChallengeResponse());
    }

    class ChallengeResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }
            JSONObject object = FastJsonUtils.parseObject(response);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.CHAPTER_NOT_REPEAT_CHALLENG.value()) {
                if (log.isErrorEnabled()) {
                    log.error("不能重复刷关卡或副本");
                }
            } else if (code == ErrorCode.SERVER_SUCCESS.value()) {
                if (log.isDebugEnabled()) {
                    log.debug("请求关卡可以挑战");
                }
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                long rid = dataObj.getLong("rid");
                Integer schoolId = dataObj.getInteger("schoolId");
                Integer chapterId = dataObj.getInteger("chapterId");
                ChallengeOverCmd challengeOverCmd = new ChallengeOverCmd();
                challengeOverCmd.setCmd(RegisterProtocol.CHAPTER_CHALLENGE_OVER_REQ);
                challengeOverCmd.setRid(rid);
                challengeOverCmd.setChapterId(chapterId);
                challengeOverCmd.setSchoolId(schoolId);
                challengeOverCmd.setState(1); //通关
                try {
//                    ProgressCmd progressCmd = new ProgressCmd();
//                    progressCmd.setCmd(RegisterProtocol.CHAPTER_PROGRESS_REQ);
//                    progressCmd.setRid(challengeCmd.rid);
//                    new ProgressRequest(progressCmd).execute();
                    Thread.sleep(10000);
                    new ChallengeOverRequest(challengeOverCmd).execute();
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("关卡挑战失败");
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
