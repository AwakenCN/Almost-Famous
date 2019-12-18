package com.noseparte.robot.enitty;

import lombok.Data;

/**
 * 职业
 */
@Data
public class Occupation {

    private Integer id;

    private String name;

    private Integer occupation;

    private Integer level;

    private Integer strength;

    private Integer agility;

    private Integer intelligence;

    private Integer lvUpExp;

    private Integer nextLvID;

    private Integer guideID;

    private String moveAI;

    private String waitAI;

    private String priority;

}


