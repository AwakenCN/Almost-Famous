package com.noseparte.robot.cardpackage;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.CardBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.RequestSync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.robot.FamousRobotApplication;
import com.noseparte.robot.Robot;
import com.noseparte.robot.RobotMgr;
import com.noseparte.robot.enitty.Card;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/9/17 11:23
 * @Description
 */
@Slf4j
public class CardListRequest extends RequestSync {

    CardListCmd cardListCmd;

    public CardListRequest(CardListCmd cardListCmd) {
        this.cardListCmd = cardListCmd;
    }

    @Override
    public void execute() throws Exception {
        sync(FamousRobotApplication.gameCoreUrl, cardListCmd.toKeyValuePair(), new CardListResponse());
    }

    class CardListResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            JSONObject object = getJSONObject(result);
            Integer code = object.getInteger("code");
            String data = object.getString("data");
            if (code == ErrorCode.SERVER_SUCCESS.value()) {
                JSONObject dataObj = FastJsonUtils.parseObject(data);
                Long rid = dataObj.getLong("rid");
                String cards = dataObj.getString("cards");
                Integer buyCnt = dataObj.getInteger("buyCnt");
                Map<Integer, CardBean> cardLst = Misc.parseToMap(cards, Integer.class, CardBean.class);
                RobotMgr robotMgr = SpringContextUtils.getBean("robotMgr", RobotMgr.class);
                Robot robot = robotMgr.getRobot(rid);
                Card cardPackage = new Card();
                cardPackage.setRid(rid);
                cardPackage.setCards(cardLst);
                cardPackage.setBuyCnt(buyCnt);
                robot.setCardPackage(cardPackage);
                if (log.isDebugEnabled()) {
                    log.debug("rid {}, 卡牌列表 {}", rid, cardLst);
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
