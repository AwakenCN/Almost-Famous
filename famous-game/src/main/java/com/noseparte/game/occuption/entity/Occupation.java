package com.noseparte.game.occuption.entity;

import lombok.Data;

/**
 * 职业
 */
@Data
public class Occupation {

    private int id;

    private String name;

    private int occupation;

    private int level;

    private int strength;

    private int agility;

    private int intelligence;

    private int lvUpExp;

    private int nextLvID;

    private int guideID;

    private String moveAI;

    private String waitAI;

    private String priority;

}


