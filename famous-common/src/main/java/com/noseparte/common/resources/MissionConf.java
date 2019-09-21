package com.noseparte.common.resources;

/**
 *
 */
public class MissionConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /********** attribute ***********/
    private Integer id;

    private String introduction;

    private Integer type;

    private Integer subType;

    private Integer condition;

    private String drop;

    private String tips;

    private String mode;

    /********** constructors ***********/
    public MissionConf() {

    }

    public MissionConf(Integer id, String introduction, Integer type, Integer subType, Integer condition, String drop, String tips, String mode) {
        this.id = id;
        this.introduction = introduction;
        this.type = type;
        this.subType = subType;
        this.condition = condition;
        this.drop = drop;
        this.tips = tips;
        this.mode = mode;
    }

    /********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
