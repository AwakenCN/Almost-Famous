package com.noseparte.robot.enitty;

import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author noseparte
 * @date 2019/8/20 17:59
 * @Description <p>角色信息表<p/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(collection = "famous-game-role")
public class Role extends GeneralBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Role() {
    }

    public Role(Long uid, Long rid, String roleName, String school, Long gold, Long silver, Long diamond, Integer lastBattleSchool, Long lastBattleCardGroup, Integer newcomerGuide, BattleRankBean battleRank) {
        this.uid = uid;
        this.rid = rid;
        this.roleName = roleName;
        this.school = school;
        this.gold = gold;
        this.silver = silver;
        this.diamond = diamond;
        this.lastBattleSchool = lastBattleSchool;
        this.lastBattleCardGroup = lastBattleCardGroup;
        this.newcomerGuide = newcomerGuide;
        this.battleRank = battleRank;
    }

    private Long uid;

    private Long rid;

    private String roleName;

    /**
     * 职业
     */
    private String school;

    private Long gold;

    private Long silver;

    private Long diamond;

    private Integer lastBattleSchool;

    private Long lastBattleCardGroup;

    /**
     * 新手引导进度
     */
    private Integer newcomerGuide;

    /**
     * 段位
     */
    private BattleRankBean battleRank;

}
