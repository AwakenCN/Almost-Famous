package com.noseparte.robot.enitty;

import com.noseparte.common.bean.BattleReportBean;
import com.noseparte.common.global.Misc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author liang
 * @since 2019-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class BattleHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    public BattleHistory(Long rid, String json) {
        this.rid = rid;
        this.reports = Misc.parseToMap(json, Long.class, BattleReportBean.class);
    }

    private Long rid;

    private Map<Long, BattleReportBean> reports;


}
