package com.noseparte.game.base;

import com.noseparte.common.bean.Action;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.game.card.controller.CardBuyAction;
import com.noseparte.game.card.controller.CardListAction;
import com.noseparte.game.card.controller.CardLockAction;
import com.noseparte.game.card.controller.CardSaleAction;
import com.noseparte.game.chapter.controller.ChapterChallengeAction;
import com.noseparte.game.chapter.controller.ChapterChallengeOverAction;
import com.noseparte.game.chapter.controller.ChapterProgressAction;
import com.noseparte.game.chapter.controller.ChapterProgressListAction;
import com.noseparte.game.mission.controller.MissionActorListAction;
import com.noseparte.game.mission.controller.MissionFinishAction;
import com.noseparte.game.pack.controller.CardBagBuyAction;
import com.noseparte.game.pack.controller.CardBagListAction;
import com.noseparte.game.pack.controller.CardBagSelectAction;
import com.noseparte.game.role.controller.*;
import com.noseparte.game.school.controller.AddOrUpdateCardGroupAction;
import com.noseparte.game.school.controller.DeleteCardGroupAction;
import com.noseparte.game.school.controller.SchoolCardGroupAction;
import com.noseparte.game.school.controller.SchoolListAction;
import com.noseparte.game.sign.controller.SignInAction;
import com.noseparte.game.sign.controller.SignRewardListAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/8/12 16:24
 * @Description
 */
public class RegisterProtocol {

    //////////////////////////心跳协议号/////////////////////////////
    public static final int HEART_BEAT_ACTION_REQ = 9991;
    public static final int HEART_BEAT_ACTION_RESP = 9992;

    ///////////////////////角色///////////////////////////
    public static final int ROLE_LIST_ACTION_REQ = 305;
    public static final int ROLE_LIST_ACTION_RESP = 306;

    public static final int CREATE_ROLE_ACTION_REQ = 307;
    public static final int CREATE_ROLE_ACTION_RESP = 308;

    public static final int ROLE_LAST_BATTLE_INFO_ACTION_REQ = 309;
    public static final int ROLE_LAST_BATTLE_INFO_ACTION_RESP = 310;

    public static final int SAVE_NEWCOMER_GUIDE_ACTION_REQ = 311;
    public static final int SAVE_NEWCOMER_GUIDE_ACTION_RESP = 312;

    public static final int GET_NEWCOMER_GUIDE_ACTION_REQ = 313;
    public static final int GET_NEWCOMER_GUIDE_ACTION_RESP = 314;

    ///////////////////////签到和奖励//////////////////////////
    public static final int SIGN_REWARD_LIST_REQ = 315;
    public static final int SIGN_REWARD_LIST_RESP = 316;

    public static final int SIGN_IN_ACTION_REQ = 317;
    public static final int SIGN_IN_ACTION_RESP = 318;

    //////////////////////属性更新///////////////////////////////
    public static final int ROLE_ATTR_ACTION_REQ = 319;
    public static final int ROLE_ATTR_ACTION_RESP = 320;

    //////////////////////战报///////////////////////////////////
    public static final int ROLE_SAVE_BATTLE_REPORT_ACTION_REQ = 321;
    public static final int ROLE_SAVE_BATTLE_REPORT_ACTION_RESP = 322;

    public static final int ROLE_BATTLE_REPORT_LIST_ACTION_REQ = 323;
    public static final int ROLE_BATTLE_REPORT_LIST_ACTION_RESP = 324;

    // 玩家段位
    public static final int ROLE_BATTLE_RANK_ACTION_REQ = 325;
    public static final int ROLE_BATTLE_RANK_ACTION_RESP = 326;

    // GM玩家货币
    public static final int ROLE_CURRENCY_CHANGE_ACTION_REQ = 327;
    public static final int ROLE_CURRENCY_CHANGE_ACTION_RESP = 328;

    ///////////////////////卡牌栏//////////////////////////
    public static final int CARD_LIST_ACTION_REQ = 401;
    public static final int CARD_LIST_ACTION_RESP = 402;

    public static final int CARD_BUY_ACTION_REQ = 403;
    public static final int CARD_BUY_ACTION_RESP = 404;

    public static final int CARD_SALE_ACTION_REQ = 405;
    public static final int CARD_SALE_ACTION_RESP = 406;

    public static final int CARD_LOCK_ACTION_REQ = 407;
    public static final int CARD_LOCK_ACTION_RESP = 408;

    ///////////////////////职业和卡组//////////////////////////
    public static final int SCHOOL_LIST_REQ = 501;
    public static final int SCHOOL_LIST_RESP = 502;

    public static final int ADD_OR_UPDATE_CARD_GROUP_REQ = 503;
    public static final int ADD_OR_UPDATE_CARD_GROUP_RESP = 504;

    public static final int SCHOOL_DELETE_CARD_GROUP_REQ = 505;
    public static final int SCHOOL_DELETE_CARD_GROUP_RESP = 506;

    public static final int SCHOOL_CARD_GROUP_REQ = 507;
    public static final int SCHOOL_CARD_GROUP_RESP = 508;
    ///////////////////////关卡和章节//////////////////////////
    public static final int CHAPTER_PROGRESS_LIST_REQ = 601;
    public static final int CHAPTER_PROGRESS_LIST_RESP = 602;

    public static final int CHAPTER_CHALLENGE_REQ = 603;
    public static final int CHAPTER_CHALLENGE_RESP = 604;

    public static final int CHAPTER_CHALLENGE_OVER_REQ = 605;
    public static final int CHAPTER_CHALLENGE_OVER_RESP = 606;

    public static final int CHAPTER_PROGRESS_REQ = 607;
    public static final int CHAPTER_PROGRESS_RESP = 608;

    ///////////////////////任务指引//////////////////////////
    public static final int MISSION_ACTOR_LIST_REQ = 701;
    public static final int MISSION_ACTOR_LIST_RESP = 702;

    public static final int MISSION_FINISH_ACTION_REQ = 703;
    public static final int MISSION_FINISH_ACTION_RESP = 704;

    ///////////////////////背包管理//////////////////////////
    public static final int CARD_BAG_BUY_ACTION_REQ = 801;
    public static final int CARD_BAG_BUY_ACTION_RESP = 802;

    public static final int CARD_BAG_SELECT_ACTION_REQ = 803;
    public static final int CARD_BAG_SELECT_ACTION_RESP = 804;

    public static final int CARD_BAG_LIST_REQ = 805;
    public static final int CARD_BAG_LIST_RESP = 806;

    public static final Map<Integer, Action> REGISTER_PROTOCOL_MAP = new HashMap<Integer, Action>() {
        {
            //心跳
            put(HEART_BEAT_ACTION_REQ, SpringContextUtils.getBean("heartbeatAction", HeartbeatAction.class));
            //角色
            put(ROLE_LIST_ACTION_REQ, SpringContextUtils.getBean("roleListAction", RoleListAction.class));
            put(CREATE_ROLE_ACTION_REQ, SpringContextUtils.getBean("createRoleAction", CreateRoleAction.class));
            put(ROLE_LAST_BATTLE_INFO_ACTION_REQ, SpringContextUtils.getBean("roleLastBattleInfoAction", RoleLastBattleInfoAction.class));
            put(SAVE_NEWCOMER_GUIDE_ACTION_REQ, SpringContextUtils.getBean("saveNewcomerGuideAction", SaveNewcomerGuideAction.class));
            put(GET_NEWCOMER_GUIDE_ACTION_REQ, SpringContextUtils.getBean("getNewcomerGuideAction", GetNewcomerGuideAction.class));
            put(ROLE_ATTR_ACTION_REQ, SpringContextUtils.getBean("roleAttrAction", RoleAttrAction.class));
//            put(ROLE_SAVE_BATTLE_REPORT_ACTION_REQ, SpringContextUtils.getBean("saveBattleReportAction", SaveBattleReportAction.class));
//            put(ROLE_BATTLE_REPORT_LIST_ACTION_REQ, SpringContextUtils.getBean("getHistoryListAction", GetHistoryListAction.class));
            put(ROLE_BATTLE_RANK_ACTION_REQ, SpringContextUtils.getBean("roleBattleRankAction", RoleBattleRankAction.class));
            put(ROLE_CURRENCY_CHANGE_ACTION_REQ, SpringContextUtils.getBean("roleCurrencyChangeAction", RoleCurrencyChangeAction.class));


            //卡牌栏
            put(CARD_LIST_ACTION_REQ, SpringContextUtils.getBean("cardListAction", CardListAction.class));
            put(CARD_BUY_ACTION_REQ, SpringContextUtils.getBean("cardBuyAction", CardBuyAction.class));
            put(CARD_SALE_ACTION_REQ, SpringContextUtils.getBean("cardSaleAction", CardSaleAction.class));
            put(CARD_LOCK_ACTION_REQ, SpringContextUtils.getBean("cardLockAction", CardLockAction.class));
            //职业和卡组
            put(SCHOOL_LIST_REQ, SpringContextUtils.getBean("schoolListAction", SchoolListAction.class));
            put(ADD_OR_UPDATE_CARD_GROUP_REQ, SpringContextUtils.getBean("addOrUpdateCardGroupAction", AddOrUpdateCardGroupAction.class));
            put(SCHOOL_DELETE_CARD_GROUP_REQ, SpringContextUtils.getBean("deleteCardGroupAction", DeleteCardGroupAction.class));
            put(SCHOOL_CARD_GROUP_REQ, SpringContextUtils.getBean("schoolCardGroupAction", SchoolCardGroupAction.class));
            //章节
            put(CHAPTER_PROGRESS_LIST_REQ, SpringContextUtils.getBean("chapterProgressListAction", ChapterProgressListAction.class));
            put(CHAPTER_CHALLENGE_REQ, SpringContextUtils.getBean("chapterChallengeAction", ChapterChallengeAction.class));
            put(CHAPTER_CHALLENGE_OVER_REQ, SpringContextUtils.getBean("chapterChallengeOverAction", ChapterChallengeOverAction.class));
            put(CHAPTER_PROGRESS_REQ, SpringContextUtils.getBean("chapterProgressAction", ChapterProgressAction.class));
            //签到和奖励
            put(SIGN_REWARD_LIST_REQ, SpringContextUtils.getBean("signRewardListAction", SignRewardListAction.class));
            put(SIGN_IN_ACTION_REQ, SpringContextUtils.getBean("signInAction", SignInAction.class));
            //任务指引
            put(MISSION_ACTOR_LIST_REQ, SpringContextUtils.getBean("missionActorListAction", MissionActorListAction.class));
            put(MISSION_FINISH_ACTION_REQ, SpringContextUtils.getBean("missionFinishAction", MissionFinishAction.class));
            //背包管理
            put(CARD_BAG_BUY_ACTION_REQ, SpringContextUtils.getBean("cardBagBuyAction", CardBagBuyAction.class));
            put(CARD_BAG_SELECT_ACTION_REQ, SpringContextUtils.getBean("cardBagSelectAction", CardBagSelectAction.class));
            put(CARD_BAG_LIST_REQ, SpringContextUtils.getBean("cardBagListAction", CardBagListAction.class));
            // 匹配机器人
//            put(MATCH_ROBOT_ACTION_REQ, SpringContextUtils.getBean("matchRobotAction", MatchRobotAction.class));
//            put(MATCH_ROBOT_BATTLE_RESULT_ACTION_REQ, SpringContextUtils.getBean("matchRobotBattleResultAction", MatchRobotBattleResultAction.class));
//            //版署
//            put(GAME_MASTER_REQ, SpringContextUtils.getBean("gameMasterAction", GameMasterAction.class));


            //其它服务器拉取角色信息
//            put(INTERNAL_BATTLE_PULL_ACTOR_ACTION_REQ, SpringContextUtils.getBean("pullActorAction", PullActorAction.class));
//            put(INTERNAL_BATTLE_START_ACTION_REQ, SpringContextUtils.getBean("battleStartAction", BattleStartAction.class));
//            put(INTERNAL_BATTLE_END_ACTION_REQ, SpringContextUtils.getBean("battleEndAction", BattleEndAction.class));
        }
    };

    public static boolean whiteList(int protocol){
        if(protocol == CREATE_ROLE_ACTION_REQ){
            return true;
        }
        return false;
    }

}
