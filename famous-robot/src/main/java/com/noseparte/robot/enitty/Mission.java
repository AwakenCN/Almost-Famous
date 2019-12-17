package com.noseparte.robot.enitty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.noseparte.common.bean.MissionBean;
import com.noseparte.common.global.Misc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author hyt
 * @since 2019-06-27
 */
@Data
@NoArgsConstructor
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    public Mission(Long rid, String json) {
        this.rid = rid;
        this.missions = Misc.parseToMap(json, Integer.class, MissionBean.class);
    }

    @TableId(type = IdType.INPUT)
    private Long rid;

    private Map<Integer, MissionBean> missions;
}
