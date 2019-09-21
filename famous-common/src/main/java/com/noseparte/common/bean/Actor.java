package com.noseparte.common.bean;

import lombok.Data;

import java.util.List;

@Data
public class Actor {
    long roleId;
    List<Integer> userCards;
    int agi;
    int iq;
    int str;
    int state;// 0 未准备好,1是准备好了
    int schoolId;// 职业id
    long groupId;// 卡组id
    String roleName;// 角色名称
    BattleRankBean battleRankBean;// 段位

}
