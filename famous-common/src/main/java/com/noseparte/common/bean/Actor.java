package com.noseparte.common.bean;

import com.noseparte.common.battle.BattleModeEnum;
import lombok.Data;

import java.util.List;

@Data
public class Actor {
    long rid;
    List<Integer> userCards;
    int agi; // 敏捷
    int iq;   // 智力
    int str;  // 力量
    int state;// 0 未准备好,1是准备好了
    int schoolId;// 职业id
    int schoolLevel;// 职业等级
    long groupId;// 卡组id
    String roleName;// 角色名称
    BattleRankBean battleRankBean;// 段位
    long matchBeginTime;//  匹配开始时间
    BattleModeEnum battleMode;// 匹配模式
}
