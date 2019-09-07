package com.liema.battle.asynchttp;

import com.liema.battle.BattleServerConfig;
import com.liema.common.http.KeyValuePair;
import com.liema.common.http.RequestAsync;
import com.liema.common.http.ResponseCallBack;
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
public class BattleStartRequest extends RequestAsync {

    List<Long> roleIds;
    long roomId;

    @Override
    public void execute() throws Exception {
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>()
        {
            {
                add(new KeyValuePair("cmd", Integer.toString(battleServerConfig.getBattleStartCmd())));
                add(new KeyValuePair("roomId", Long.toString(roomId)));
                add(new KeyValuePair("rids", roleIds.toString()));
            }
        };

        async(battleServerConfig.getGameUrl(), postBody, new BattleStartResponse());
        if (log.isDebugEnabled()) {
            log.debug("Send battleStartRequest to gamecore, roomId={}", roomId);
        }
    }

    class BattleStartResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }
        }

        @Override
        public void failed(Exception ex) {
            log.error("Battle start Request fail.");
        }

        @Override
        public void cancelled() {

        }
    }
}
