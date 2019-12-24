package com.noseparte.match.asynchttp;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.battle.BattleModeEnum;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.bean.Actor;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.http.KeyValuePair;
import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.FastJsonUtils;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.match.MatchServerConfig;
import com.noseparte.match.match.LeisureMatchMgr;
import com.noseparte.match.match.RankMatchMgr;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Noseparte
 * @Date: 2019-12-22 12:59
 * @Description:
 *
 *          <p>拉取角色出战数据</p>
 *
 */

@Slf4j
@RequiredArgsConstructor
public class LoadActorRequest extends RequestAsync {

    @Getter
    @Setter
    @NonNull
    Session session;

    @Getter
    @Setter
    @NonNull
    BattleModeEnum battleMode;

    @Override
    public void execute() throws Exception {
        MatchServerConfig matchServerConfig = SpringContextUtils.getBean("matchServerConfig", MatchServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>() {
            {
                add(new KeyValuePair("cmd", Integer.toString(matchServerConfig.getPullActorCmd())));
                add(new KeyValuePair("rid", Long.toString(session.getRid())));
                add(new KeyValuePair("uid", Long.toString(session.getUid())));
                add(new KeyValuePair("actor", FastJsonUtils.toJson(session)));
            }
        };

        List<KeyValuePair> headers = new ArrayList<KeyValuePair>() {
            {
                add(new KeyValuePair("access_token", session.getToken()));
            }
        };

        async(matchServerConfig.getGameCoreUrl(), postBody, headers, new LoadActorResponse());
    }

    @Data
    @AllArgsConstructor
    class LoadActorResponse implements ResponseCallBack<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }

            JSONObject obj = FastJsonUtils.parseObject(response);
            int code = obj.getInteger("code");
            if (code != ErrorCode.SERVER_SUCCESS.value()) {
                log.error("匹配获取角色信息失败.");
                return;
            }
            String data = obj.getString("actor");
            Actor actor = FastJsonUtils.toBean(data, Actor.class);
            actor.setBattleMode(battleMode);// 匹配模式
            long matchBeginTime = System.currentTimeMillis();
            actor.setMatchBeginTime(matchBeginTime);// 匹配开始时间

            // 添加到匹配队列
            if (battleMode == BattleModeEnum.LEISURE) {
                LeisureMatchMgr leisureMatchMgr = SpringContextUtils.getBean("leisureMatchMgr", LeisureMatchMgr.class);
                leisureMatchMgr.add(actor);
            } else if (battleMode == BattleModeEnum.RANK) {
                RankMatchMgr rankMatchMgr = SpringContextUtils.getBean("rankMatchMgr", RankMatchMgr.class);
                rankMatchMgr.add(actor);
            }

        }

        @Override
        public void failed(Exception e) {
            log.error("Load Actor Request fail.", e);
        }

        @Override
        public void cancelled() {

        }
    }
}
