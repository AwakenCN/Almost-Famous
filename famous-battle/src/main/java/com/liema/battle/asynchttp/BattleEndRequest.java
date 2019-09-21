package com.liema.battle.asynchttp;

import com.liema.battle.BattleServerConfig;
import com.liema.common.http.KeyValuePair;
import com.liema.common.http.RequestAsync;
import com.liema.common.http.ResponseCallBack;
import com.liema.common.utils.FastJsonUtils;
import com.liema.common.utils.SpringContextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
public class BattleEndRequest extends RequestAsync {

    long roomId;
    List<Long> winners;
    List<Long> losers;

    @Override
    public void execute() throws Exception {
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>()
        {
            {
                add(new KeyValuePair("cmd", Integer.toString(battleServerConfig.getBattleEndCmd())));
                add(new KeyValuePair("roomId", Long.toString(roomId)));
                add(new KeyValuePair("winners", FastJsonUtils.toJson(winners)));
                add(new KeyValuePair("losers", FastJsonUtils.toJson(losers)));
            }
        };

        async(battleServerConfig.getGameUrl(), postBody, new BattleEndResponse());
        if (log.isDebugEnabled()) {
            log.debug("Send battleEndRequest to gamecore, roomId={}", roomId);
        }
    }

    class BattleEndResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }
        }

        @Override
        public void failed(Exception ex) {
            log.error("Battle end Request fail.");
        }

        @Override
        public void cancelled() {

        }
    }

}
