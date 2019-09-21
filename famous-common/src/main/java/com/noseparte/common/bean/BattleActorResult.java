package com.noseparte.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleActorResult {
    public static final int NORMAL = 0;
    public static final int EXCEPTION = 1;
    public static final int SURRENDER = 2;

    long ownerId;
    long uploadTime;
    int overState;// 结束状态   0=normal, 1=exception, 2=surrender
    BattleReportBean battleReport;
}
