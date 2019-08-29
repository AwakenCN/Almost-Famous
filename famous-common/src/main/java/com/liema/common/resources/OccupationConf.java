package com.liema.common.resources;

/**
 *
 */
public class OccupationConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /********** attribute ***********/
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

    /********** constructors ***********/
    public OccupationConf() {

    }

    public OccupationConf(Integer id, String name, Integer occupation, Integer level, Integer strength, Integer agility, Integer intelligence, Integer lvUpExp, Integer nextLvID, Integer guideID, String moveAI, String waitAI, String priority) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
        this.level = level;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.lvUpExp = lvUpExp;
        this.nextLvID = nextLvID;
        this.guideID = guideID;
        this.moveAI = moveAI;
        this.waitAI = waitAI;
        this.priority = priority;
    }

    /********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getLvUpExp() {
        return lvUpExp;
    }

    public void setLvUpExp(Integer lvUpExp) {
        this.lvUpExp = lvUpExp;
    }

    public Integer getNextLvID() {
        return nextLvID;
    }

    public void setNextLvID(Integer nextLvID) {
        this.nextLvID = nextLvID;
    }

    public Integer getGuideID() {
        return guideID;
    }

    public void setGuideID(Integer guideID) {
        this.guideID = guideID;
    }

    public String getMoveAI() {
        return moveAI;
    }

    public void setMoveAI(String moveAI) {
        this.moveAI = moveAI;
    }

    public String getWaitAI() {
        return waitAI;
    }

    public void setWaitAI(String waitAI) {
        this.waitAI = waitAI;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
