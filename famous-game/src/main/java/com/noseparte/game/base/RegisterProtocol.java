package com.noseparte.game.base;

import com.noseparte.common.bean.Action;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.game.role.controller.*;

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
            //角色
            put(ROLE_LIST_ACTION_REQ, SpringContextUtils.getBean("roleListAction", RoleListAction.class));
            put(CREATE_ROLE_ACTION_REQ, SpringContextUtils.getBean("createRoleAction", CreateRoleAction.class));
            put(ROLE_LAST_BATTLE_INFO_ACTION_REQ, SpringContextUtils.getBean("roleLastBattleInfoAction", RoleLastBattleInfoAction.class));
            put(SAVE_NEWCOMER_GUIDE_ACTION_REQ, SpringContextUtils.getBean("saveNewcomerGuideAction", SaveNewcomerGuideAction.class));
            put(GET_NEWCOMER_GUIDE_ACTION_REQ, SpringContextUtils.getBean("getNewcomerGuideAction", GetNewcomerGuideAction.class));
            put(ROLE_ATTR_ACTION_REQ, SpringContextUtils.getBean("roleAttrAction", RoleAttrAction.class));
            put(ROLE_BATTLE_RANK_ACTION_REQ, SpringContextUtils.getBean("roleBattleRankAction", RoleBattleRankAction.class));
            put(ROLE_CURRENCY_CHANGE_ACTION_REQ, SpringContextUtils.getBean("roleCurrencyChangeAction", RoleCurrencyChangeAction.class));

        }
    };

    public static boolean whiteList(int protocol){
        if(protocol == CREATE_ROLE_ACTION_REQ){
            return true;
        }
        return false;
    }
}
