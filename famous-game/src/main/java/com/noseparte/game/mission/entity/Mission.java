package com.noseparte.game.mission.entity;

import com.noseparte.common.bean.MissionBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "famous-game-mission")
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    public Mission(Long rid, Map<Integer, MissionBean> missions) {
        this.rid = rid;
        this.missions = missions;
    }

    private Long rid;

    private Map<Integer, MissionBean> missions;
}
