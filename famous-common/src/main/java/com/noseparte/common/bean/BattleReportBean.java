package com.noseparte.common.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BattleReportBean {
    private long roomId;
    private List<Long> winners;
    private List<Long> losers;
    long createTime;// 战斗开始时间
    long battleTime;// 战斗持续时间
    int state;//  0 未结束， 1结束

    public BattleReportBean(long roomId, List<Long> winners, List<Long> losers, long createTime, long battleTime, int state) {
        this.roomId = roomId;
        this.winners = winners;
        this.losers = losers;
        this.createTime = createTime;
        this.battleTime = battleTime;
        this.state = state;
    }
}
