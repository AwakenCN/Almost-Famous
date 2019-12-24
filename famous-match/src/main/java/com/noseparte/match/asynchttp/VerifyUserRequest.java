package com.noseparte.match.asynchttp;

import com.noseparte.common.battle.BattleModeEnum;
import com.noseparte.common.battle.SimpleBattleRoom;
import com.noseparte.common.battle.server.LinkMgr;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.common.battle.server.Session;
import com.noseparte.common.global.Misc;
import com.noseparte.common.http.KeyValuePair;
import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.match.MatchServerConfig;
import com.noseparte.match.match.RankMatchMgr;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Noseparte
 * @Date: 2019-12-22 12:47
 * @Description:
 *
 *          <p>验证登录</p>
 *          <p>异步获取用户信息</p>
 *          <p>然后缓存到session中</p>
 */

@Slf4j
@Setter
@Getter
@AllArgsConstructor
public class VerifyUserRequest extends RequestAsync {
    long userId;
    long roleId;
    String token;
    Channel channel;
    int schoolId;
    long groupId;
    BattleModeEnum battleMode;

    public void execute() throws Exception {
        MatchServerConfig matchServerConfig = SpringContextUtils.getBean("matchServerConfig", MatchServerConfig.class);
        List<KeyValuePair> postBody = new ArrayList<KeyValuePair>() {
            {
                add(new KeyValuePair("cmd", matchServerConfig.getVerifyUserLoginCmd() + ""));
                add(new KeyValuePair("userId", Long.toString(userId)));
                add(new KeyValuePair("token", token));
            }
        };
        async(matchServerConfig.getAdminUrl(), postBody, new VerifyUserResponse());
    }

    @Data
    class VerifyUserResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {
            String response = getHttpContent(result);
            if (log.isDebugEnabled()) {
                log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
            }
            //验证通过添加到在线列表
            Session session = new Session();
            session.setRid(roleId);
            session.setIsLogin((byte) 1);
            session.setChannel(channel);
            session.setToken(token);
            session.setUid(userId);
            session.setSid(Misc.getSid(channel));
            session.setSchoolId(schoolId);
            session.setGroupId(groupId);
            LinkMgr.addSession(session);

            try {
                RankMatchMgr rankMatchMgr = SpringContextUtils.getBean("rankMatchMgr", RankMatchMgr.class);
                SimpleBattleRoom room = rankMatchMgr.getSimpleBattleRoomByRoleId(roleId);
                if (null != room) {
                    Protocol p = rankMatchMgr.toS2CMatch(room);
                    if (log.isInfoEnabled()) {
                        log.info("补发送战斗房间信息, role={}, room={}", roleId, room.getRoomId());
                    }
                    LinkMgr.send(session.getSid(), p);
                    return;
                }

                // 拉取角色信息
                try {
                    new LoadActorRequest(session, battleMode).execute();
                } catch (Exception e) {
                    log.error("pull actor error : ", e);
                }
            } catch (Exception e) {
                log.error("{completed} Verify User Request fail.", e);
            }

        }

        @Override
        public void failed(Exception e) {
            log.error("{failed} Verify User Request fail.", e);
        }

        @Override
        public void cancelled() {

        }
    }
}

