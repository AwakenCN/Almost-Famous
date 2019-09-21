package com.noseparte.battle.asynchttp;

import com.noseparte.battle.BattleServerConfig;
import com.noseparte.battle.battle.BattleRoom;
import com.noseparte.battle.battle.BattleRoomMgr;
import com.noseparte.battle.match.MatchMgr;
import com.noseparte.battle.server.LinkMgr;
import com.noseparte.battle.server.Protocol;
import com.noseparte.battle.server.Session;
import com.noseparte.common.http.KeyValuePair;
import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.SpringContextUtils;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
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
/**
 * 验证登录
 */
public class VerifyUserRequest extends RequestAsync {
    long userId;
    long roleId;
    String token;
    Channel channel;
    int schoolId;
    long groupId;

    public void execute() throws Exception {
        BattleServerConfig battleServerConfig = SpringContextUtils.getBean("battleServerConfig", BattleServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>() {
            {
                add(new KeyValuePair("cmd", battleServerConfig.getVerifyUserLoginCmd() + ""));
                add(new KeyValuePair("userId", Long.toString(userId)));
                add(new KeyValuePair("roleId", Long.toString(roleId)));
                add(new KeyValuePair("schoolId", Long.toString(schoolId)));
                add(new KeyValuePair("groupId", Long.toString(groupId)));
                add(new KeyValuePair("token", token));
            }
        };
        async(battleServerConfig.getLoginUrl(), postBody, new VerifyUserResponse(userId, roleId, token, channel, schoolId, groupId));
    }

    @Data
    @AllArgsConstructor
    class VerifyUserResponse implements ResponseCallBack<HttpResponse> {
        long userId;
        long roleId;
        String token;
        Channel channel;
        int schoolId;
        long groupId;

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }
            //验证通过添加到在线列表
            Session session = new Session();
            session.setRoleId(roleId);
            session.setIsLogin((byte) 1);
            session.setChannel(channel);
            session.setToken(token);
            session.setUserId(userId);
            session.setSid(channel.id().asShortText().hashCode());
            session.setSchoolId(schoolId);
            session.setGroupId(groupId);
            LinkMgr.addSession(session);
//          HttpClientUtils.closeQuietly(response);

            BattleRoomMgr battleMgr = SpringContextUtils.getBean("battleRoomMgr", BattleRoomMgr.class);
            // 验证是否有存在的房间
            if (battleMgr.isHaveRoom(roleId)) {
                BattleRoom room = battleMgr.getBattleRoomByRoleId(roleId);
                Protocol p = room.toS2CMatch();
                if (log.isInfoEnabled()) {
                    log.info("补发送战斗房间信息, role={}, room={}", roleId, room.getRoomId());
                }
                LinkMgr.send(session.getSid(), p);
                return;
            }
            //加入到匹配队列
            MatchMgr matchMgr = SpringContextUtils.getBean("matchMgr", MatchMgr.class);
            matchMgr.addMatchQueue(session);
        }

        @Override
        public void failed(Exception ex) {
            log.error("Verify User Request fail.");
        }

        @Override
        public void cancelled() {

        }
    }
}

