package com.noseparte.robot.role;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class RoleListRequest extends RequestAsync {

    private RoleListCmd roleListCmd;

    public RoleListRequest(RoleListCmd roleListCmd) {
        this.roleListCmd = roleListCmd;
    }

    @Override
    public void execute() throws Exception {
        async(FamousRobotApplication.gameCoreUrl, roleListCmd.toKeyValuePair(), new RoleListResponse());
    }

    class RoleListResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            JSONObject obj = getJSONObject(result);
            int code = obj.getInteger("code");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                String data = obj.getString("data");
                if (data == null) {
                    CreateRoleCmd createRoleCmd = new CreateRoleCmd();
                    createRoleCmd.setCmd(RegisterProtocol.CREATE_ROLE_ACTION_REQ);
                    createRoleCmd.setUid(roleListCmd.getUid());
                    createRoleCmd.setToken(roleListCmd.getToken());
                    createRoleCmd.setIndex(roleListCmd.getIndex());
                    try {
                        new CreateRoleRequest(createRoleCmd).execute();
                    } catch (Exception e) {
                        log.error("Create role error: ", e);
                    }
                    return;
                }
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                long rid = dataObj.getLong("rid");
                if (rid > 0) {
                    Robot robot = new Robot();
                    robot.setUid(roleListCmd.getUid());
                    robot.setToken(roleListCmd.getToken());
                    robot.setRid(rid);
                    robot.setRoleName(dataObj.getString("roleName"));
                    robot.setSchool(dataObj.getString("school"));
                    robot.setGold(dataObj.getLong("gold"));
                    robot.setSilver(dataObj.getLong("silver"));
                    robot.setDiamond(dataObj.getLong("diamond"));
                    RobotMgr.getInstance().addRobot(robot);

                    FamousRobotApplication.pool.execute(robot, roleListCmd.getIndex());


                } else {
                    log.error("没有角色信息??");
                }
            }
        }

        @Override
        public void failed(Exception ex) {
            log.error("", ex);
        }

        @Override
        public void cancelled() {
            log.error("cancelled");
        }
    }


}
