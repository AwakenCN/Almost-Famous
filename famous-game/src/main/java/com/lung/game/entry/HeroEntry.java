package com.lung.game.entry;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author haoyitao
 * @implSpec 英雄类
 * @since 2023/9/25 - 15:16
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class HeroEntry {

    // 英雄ID
    private int id;
    // 玩家ID
    private String uid;
    // 名字
    private String name;
    // 类型
    private String type;
    // 等级
    private String lv;
    // 星级
    private String star;
    // 经验
    private int exp;
    // 血量
    private int hp;





}
