package com.lung.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeroConfig {

    private int id;
    private String name;
    private int attack;
    private int defend;
    private int speed;
    private int intellect;
    // 类型
    private String type;

    public HeroConfig(int id, String name, int attack, int defend, int speed, int intellect, int heroType) {
        this.id = id;
        this.name = name;
        this.attack = attack;
        this.defend = defend;
        this.speed = speed;
        this.intellect = intellect;
        this.type = getTypeName(heroType);
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
