package com.noseparte.robot.bag;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.RegisterProtocol;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

/**
 * @Auther: Noseparte
 * @Date: 2019/10/22 10:09
 * @Description:
 *
 *          <p>robot 异步抽卡</p>
 */
@Slf4j
public class BagSelectRequest extends RequestSync {

    private BagSelectCmd bagSelectCmd;

    public BagSelectRequest(BagSelectCmd bagSelectCmd) {
        this.bagSelectCmd = bagSelectCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, bagSelectCmd.toKeyValuePair(), new BagSelectResponse());
    }

    class BagSelectResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject jsonObject = getJSONObject(result);
            Integer code = jsonObject.getInteger("code");
            String data = jsonObject.getString("data");
            if(code == ErrorCode.SERVER_SUCCESS.value()){
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String cards = dataObj.getString("cards");
                Integer attrId = dataObj.getInteger("attrId");
                Integer attrVal = dataObj.getInteger("attrVal");
                if(log.isInfoEnabled()){
                    log.info("robot {}, 抽到的卡， {}， {}: {}", rid, cards, attrId, attrVal);
                }
                BagListCmd bagListCmd = new BagListCmd();
                bagListCmd.setCmd(RegisterProtocol.CARD_BAG_LIST_REQ);
                bagListCmd.setRid(rid);
                try {
                    new BagListRequest(bagListCmd).execute();
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("robot {} 抽卡失败", rid);
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
