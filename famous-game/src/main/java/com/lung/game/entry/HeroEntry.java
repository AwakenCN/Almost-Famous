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

    private int id;
    private String name;
    private int attack;
    private int defend;
    private int speed;
    private int intellect;
    private String type;

    public HeroEntry(int id, String name, int attack, int defend, int speed, int intellect) {
        this.id = id;
        this.name = name;
        this.attack = attack;
        this.defend = defend;
        this.speed = speed;
        this.intellect = intellect;
    }

    public HeroEntry(int id, String name, int attack, int defend, int speed, int intellect, int type) {
        this(id, name, attack, defend, speed, intellect);
        this.type = getTypeName(type);
    }

    private String getTypeName(int type) {
        if (type == 1) {
            return "力量型";
        } else if (type == 2) {
            return "敏捷型";
        } else if (type == 3) {
            return "智力型";
        }
        return "";
    }



}
