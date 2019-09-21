package com.noseparte.common.bean;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * 任务类型
 */
public enum MissionCode {

    // 主线关卡进度
    MAIN_LEVEL_PROGRESS(1),
    // 某职业等级
    GRADE_OF_OCCUPATIONS(2),
    // 职业总等级
    TOTAL_GRADE_OCCUPATIONS(3),
    // 使用某职业进行对战
    NUMBER_OF_OCCUPATION(4),
    // 某模式对战次数
    NUMBER_OF_PARTICULAR_MODE(5),
    // 进行抽卡
    DRAW_CARD(6),
    // 购买卡包
    PURCHASE_CARD_PACKAGE(7),
    // 段位
    DAN_GRADING(8);

    private int type;

    @Immutable
    MissionCode(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }

}
