package com.noseparte.game.role.entity;

import com.noseparte.common.bean.BattleRankBean;
import com.noseparte.common.db.pojo.GeneralBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author noseparte
 * @link github.com/noseparte
 * @date 2020/11/19 - 20:34
 * @implSpec <p>角色信息表<p/>
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "famous-game-role")
public class Role extends GeneralBean {

    @Id
    @Field("_id")
    private Long uid;

    /**
     * role identifier
     */
    @Field("rid")
    private Long rid;

    /**
     * 玩家昵称
     */
    @Field("roleName")
    private String roleName;

    /**
     * 职业
     */
    @Field("school")
    private String school;

    /**
     * 金币
     */
    @Field("gold")
    private Long gold;

    /**
     * 银币
     */
    @Field("silver")
    private Long silver;

    /**
     * 钻石
     */
    @Field("diamond")
    private Long diamond;

    /**
     * 上次出战职业
     */
    @Field("lastBattleSchool")
    private Integer lastBattleSchool;

    /**
     * 上次出战卡组
     */
    @Field("lastBattleCardGroup")
    private Long lastBattleCardGroup;

    /**
     * 新手引导进度
     */
    @Field("newcomerGuide")
    private Integer newcomerGuide;

    /**
     * 段位
     */
    @Field("battleRank")
    private BattleRankBean battleRank;

}
