package com.noseparte.robot.gm;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.robot.FamousRobotApplication;
import org.apache.http.HttpResponse;

/**
 * @Auther: Noseparte
 * @Date: 2019/12/27 14:46
 * @Description: <p></p>
 */
public class GameMasterRequest extends RequestSync {

    private GameMasterCmd gameMasterCmd;

    public GameMasterRequest(GameMasterCmd gameMasterCmd) {
        this.gameMasterCmd = gameMasterCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, gameMasterCmd.toKeyValuePair(), new GameMasterResponse());
    }

    @Override
    public JSONObject callback() throws Exception {
        return syncCallBack(FamousRobotApplication.gameCoreUrl, gameMasterCmd.toKeyValuePair());
    }

    private class GameMasterResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
//            JSONObject jsonObject = getJSONObject(result);
//            Integer code = jsonObject.getInteger("code");
//            String data = jsonObject.getString("data");
//            // GM 执行成功
//            if(code == ErrorCode.SERVER_SUCCESS.value()){
//                JSONObject dataObj = JSON.parseObject(data);
//
//                // 关卡
//                ProgressCmd progressCmd = new ProgressCmd();
//                progressCmd.setCmd(RegisterProtocol.CHAPTER_PROGRESS_REQ);
//                progressCmd.setRid(gameMasterCmd.getRid());
//                try {
//                    new ProgressRequest(progressCmd).execute();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }



        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }
}
