package com.noseparte.common.resources;

/**
 *
 */
public class OccupationConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private Integer isOcc;

    private String name;

    private Integer defaultWeapon;

    private Integer occupation;

    private Integer level;

    private Integer strength;

    private Integer agility;

    private Integer intelligence;

    private Integer lvUpExp;

    private Integer minExp;

    private Integer maxExp;

    private Integer lastLvID;

    private Integer nextLvID;

    private Integer guideID;

    private Integer rankID;

    private Integer star;

    private Integer cardGroup;

/********** constructors ***********/
    public OccupationConf() {

    }

    public OccupationConf(Integer id, Integer isOcc, String name, Integer defaultWeapon, Integer occupation, Integer level, Integer strength, Integer agility, Integer intelligence, Integer lvUpExp, Integer minExp, Integer maxExp, Integer lastLvID, Integer nextLvID, Integer guideID, Integer rankID, Integer star, Integer cardGroup) {
        this.id = id;
        this.isOcc = isOcc;
        this.name = name;
        this.defaultWeapon = defaultWeapon;
        this.occupation = occupation;
        this.level = level;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.lvUpExp = lvUpExp;
        this.minExp = minExp;
        this.maxExp = maxExp;
        this.lastLvID = lastLvID;
        this.nextLvID = nextLvID;
        this.guideID = guideID;
        this.rankID = rankID;
        this.star = star;
        this.cardGroup = cardGroup;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsOcc() {
        return isOcc;
    }

    public void setIsOcc(Integer isOcc) {
        this.isOcc = isOcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultWeapon() {
        return defaultWeapon;
    }

    public void setDefaultWeapon(Integer defaultWeapon) {
        this.defaultWeapon = defaultWeapon;
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

    public Integer getMinExp() {
        return minExp;
    }

    public void setMinExp(Integer minExp) {
        this.minExp = minExp;
    }

    public Integer getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(Integer maxExp) {
        this.maxExp = maxExp;
    }

    public Integer getLastLvID() {
        return lastLvID;
    }

    public void setLastLvID(Integer lastLvID) {
        this.lastLvID = lastLvID;
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

    public Integer getRankID() {
        return rankID;
    }

    public void setRankID(Integer rankID) {
        this.rankID = rankID;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getCardGroup() {
        return cardGroup;
    }

    public void setCardGroup(Integer cardGroup) {
        this.cardGroup = cardGroup;
    }

}
