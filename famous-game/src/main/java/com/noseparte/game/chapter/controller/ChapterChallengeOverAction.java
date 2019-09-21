package com.noseparte.game.chapter.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.bean.PropBean;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Misc;
import com.noseparte.common.global.Resoult;
import com.noseparte.game.base.GameUtils;
import com.noseparte.game.base.RegisterProtocol;
import com.noseparte.game.base.SendMessage;
import com.noseparte.game.chapter.service.ChapterService;
import com.noseparte.game.item.HoldItem;
import com.noseparte.game.item.ItemMgr;
import com.noseparte.game.mission.service.MissionService;
import com.noseparte.game.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChapterChallengeOverAction extends Action {

    @Autowired
    ChapterService chapterService;
    @Autowired
    MissionService missionService;
    @Autowired
    RoleService roleService;
    @Autowired
    SendMessage sendMessage;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Integer chapterId = jsonObject.getInteger("chapterId");
        Integer schoolId = jsonObject.getInteger("schoolId");
        Integer state = jsonObject.getInteger("state"); //失败 0

        JSONObject object = chapterService.challengeOver(rid, schoolId, chapterId, state);
        ErrorCode code = object.getObject("code", ErrorCode.class);
        String drop = object.getString("drop");
        if (code != ErrorCode.SERVER_SUCCESS) {
            return Resoult.error(RegisterProtocol.CHAPTER_CHALLENGE_OVER_RESP, code, "");
        }

        JSONObject data = new JSONObject();
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        String cutQuotes = Misc.cutQuotes(drop);
        List<HoldItem> holdItems = ItemMgr.dropItem(cutQuotes);
        List<PropBean> propList = GameUtils.getPropList(holdItems);
        data.put("drop", propList);

        //推送GM
        Resoult result = GameUtils.getActorCurrency(roleService.selectByRoleId(rid), rid);
        sendMessage.send(rid, result);
//        sendMessage.sendNow(rid);

        missionService.actorMissionMgr(missionService.getActorMissionById(rid), roleService.selectByRoleId(rid));
        //任务推送
//        missionService.noticeMission(rid);
        return Resoult.ok(RegisterProtocol.CHAPTER_CHALLENGE_OVER_RESP).responseBody(data);
    }
}
