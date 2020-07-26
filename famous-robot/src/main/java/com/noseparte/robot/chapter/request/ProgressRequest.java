package com.noseparte.robot.chapter.request;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.ChapterBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.chapter.cmd.ProgressCmd;
import com.noseparte.robot.enitty.Chapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/16 12:42
 * @Description: <p>玩家 关卡进度</p>
 * > 注意：区别于 ProgressListRequest
 */
@Slf4j
public class ProgressRequest extends RequestSync {

    private ProgressCmd progressCmd;

    public ProgressRequest(ProgressCmd progressCmd) {
        this.progressCmd = progressCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, progressCmd.toKeyValuePair(), new ProgressResponse());
    }

    class ProgressResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String chapters = dataObj.getString("chapters");
                Map<Integer, ChapterBean> chapterMap = Misc.parseToMap(chapters, Integer.class, ChapterBean.class);
                RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
                Robot robot = robotMgr.getRobotMap().get(rid);
                Chapter chapter = new Chapter();
                chapter.setRid(rid);
                chapter.setChapters(chapterMap);
                robot.setChapter(chapter);
                if (log.isInfoEnabled()) {
                    log.info("进度列表 {}", chapterMap);
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
