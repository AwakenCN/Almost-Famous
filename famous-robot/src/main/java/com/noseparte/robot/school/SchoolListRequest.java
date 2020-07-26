package com.noseparte.robot.school;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.SchoolBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.enitty.School;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 11:56
 * @Description
 */
@Slf4j
public class SchoolListRequest extends RequestSync {

    SchoolListCmd schoolListCmd;

    public SchoolListRequest(SchoolListCmd schoolListCmd) {
        this.schoolListCmd = schoolListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, schoolListCmd.toKeyValuePair(), new SchoolListResponse());
    }

    class SchoolListResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SCHOOL_NOT_EXIST.value()) {
                if (log.isErrorEnabled()) {
                    log.error("获取签到信息失败");
                }
            }
            JSONObject dataObj = FastJsonUtils.parseObject(data);
            Long rid = dataObj.getLong("rid");
            String schools = dataObj.getString("schools");
            Map<Integer, SchoolBean> schoolLst = Misc.parseToMap(schools, Integer.class, SchoolBean.class);
            RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
            Robot robot = robotMgr.getRobot(rid);
            School school = new School();
            school.setRid(rid);
            school.setSchools(schoolLst);
            robot.setOccupation(school);
            if (log.isDebugEnabled()) {
                log.debug("rid {}, 职业列表, {}", rid, schoolLst);
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
