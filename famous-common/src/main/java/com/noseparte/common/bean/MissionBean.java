package com.noseparte.common.bean;

import lombok.Data;

@Data
public class MissionBean implements Comparable<MissionBean> {

    public final static int MIN = 0;
    public final static int MAX = 3;
    ///////////////////////////// 任务类型
    // 主线关卡进度
    public final static int MAIN_LEVEL_PROGRESS = 1;
    // 某职业等级
    public final static int GRADE_OF_OCCUPATIONS = 2;
    // 职业总等级
    public final static int TOTAL_GRADE_OCCUPATIONS = 3;
    // 使用某职业进行对战
    public final static int NUMBER_OF_OCCUPATION = 4;
    // 某模式对战次数
    public final static int NUMBER_OF_PARTICULAR_MODE = 5;
    // 进行抽卡
    public final static int DRAW_CARD = 6;
    // 购买卡包
    public final static int PURCHASE_CARD_PACKAGE = 7;
    // 段位
    public final static int DAN_GRADING = 8;

    // 任务id
    private int missionId;
    // 任务状态
    private int status;
    // actor对应的进度
    private int condition;
    // 完成时间
    private Long completeTick;
    // default 1：任务队列中保留 or：0
    private int isShow = 1;

    @Override
    public int compareTo(MissionBean o) {
        if (o.getStatus() > status) {
            return 1;
        } else if (o.getStatus() < status) {
            return -1;
        } else {
            if (missionId < o.getMissionId()) {
                return -1;
            }else{
                return 1;
            }
        }
    }
}
