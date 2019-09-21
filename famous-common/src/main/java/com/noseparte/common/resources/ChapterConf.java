package com.noseparte.common.resources;

/**
 *
 */
public class ChapterConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String bossName;

    private Integer bossVocation;

    private String bossAttr;

    private String bossCard;

    private String drop;

    private String failDrop;

    private Integer mapId;

    private String guideId;

    private String triggerId;

    private Integer markId;

    private Integer groupId;

    private Integer vocationId;

/********** constructors ***********/
    public ChapterConf() {

    }

    public ChapterConf(Integer id, String bossName, Integer bossVocation, String bossAttr, String bossCard, String drop, String failDrop, Integer mapId, String guideId, String triggerId, Integer markId, Integer groupId, Integer vocationId) {
        this.id = id;
        this.bossName = bossName;
        this.bossVocation = bossVocation;
        this.bossAttr = bossAttr;
        this.bossCard = bossCard;
        this.drop = drop;
        this.failDrop = failDrop;
        this.mapId = mapId;
        this.guideId = guideId;
        this.triggerId = triggerId;
        this.markId = markId;
        this.groupId = groupId;
        this.vocationId = vocationId;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public Integer getBossVocation() {
        return bossVocation;
    }

    public void setBossVocation(Integer bossVocation) {
        this.bossVocation = bossVocation;
    }

    public String getBossAttr() {
        return bossAttr;
    }

    public void setBossAttr(String bossAttr) {
        this.bossAttr = bossAttr;
    }

    public String getBossCard() {
        return bossCard;
    }

    public void setBossCard(String bossCard) {
        this.bossCard = bossCard;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getFailDrop() {
        return failDrop;
    }

    public void setFailDrop(String failDrop) {
        this.failDrop = failDrop;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getVocationId() {
        return vocationId;
    }

    public void setVocationId(Integer vocationId) {
        this.vocationId = vocationId;
    }

}
