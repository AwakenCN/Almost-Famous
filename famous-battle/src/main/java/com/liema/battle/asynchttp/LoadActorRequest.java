package com.liema.battle.asynchttp;

import com.alibaba.fastjson.JSONObject;
import com.liema.battle.BattleServerConfig;
import com.liema.battle.battle.BattleRoom;
import com.liema.battle.battle.BattleRoomMgr;
import com.liema.battle.server.LinkMgr;
import com.liema.battle.server.Protocol;
import com.liema.battle.server.Session;
import com.liema.common.bean.Actor;
import com.liema.common.http.KeyValuePair;
import com.liema.common.http.RequestAsync;
import com.liema.common.http.ResponseCallBack;
import com.liema.common.utils.FastJsonUtils;
import com.liema.common.utils.SpringContextUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
/**
 * 拉取角色出战数据
 */
public class LoadActorRequest extends RequestAsync {

    @Getter
    @Setter
    @NonNull
    List<Session> sessions;

    @Override
    public void execute() throws Exception {
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>() {
            {
                add(new KeyValuePair("cmd", Integer.toString(battleServerConfig.getPullActorCmd())));
                add(new KeyValuePair("actors", FastJsonUtils.toJson(sessions)));
            }
        };

        async(battleServerConfig.getGameUrl(), postBody, new PullUserResponse(sessions));
    }

    @Data
    @AllArgsConstructor
    class PullUserResponse implements ResponseCallBack<HttpResponse> {
        List<Session> actors;

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }

            JSONObject obj = FastJsonUtils.parseObject(response);
            String msg = obj.getString("msg");
            String code = obj.getString("code");
            String data = obj.getString("actors");
            List<Actor> actors = FastJsonUtils.toList(data, Actor.class);

            // 创建战斗房间
            BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
            BattleRoom battleRoom = battleMgr.createBattleRoom(actors);
            battleMgr.addBattleRoom(battleRoom);
            Protocol p = battleRoom.toS2CMatch();

            // 返回客户端演员信息
            for (Session session : sessions) {
                LinkMgr.send(session.getSid(), p);
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
