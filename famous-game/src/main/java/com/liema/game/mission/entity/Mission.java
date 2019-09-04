package com.liema.game.mission.entity;

import com.liema.common.bean.MissionBean;
import com.liema.common.global.Misc;
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

    private Long rid;

    private Map<Integer, MissionBean> missions;
}
