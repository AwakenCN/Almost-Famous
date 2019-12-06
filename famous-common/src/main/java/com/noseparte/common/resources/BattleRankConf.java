package com.noseparte.common.resources;

/**
 *
 */
public class BattleRankConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String name;

    private Integer icon;

    private Integer minstar;

    private Integer maxstar;

    private Integer starLimit;

    private Integer isProtect;

/********** constructors ***********/
    public BattleRankConf() {

    }

    public BattleRankConf(Integer id, String name, Integer icon, Integer minstar, Integer maxstar, Integer starLimit, Integer isProtect) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.minstar = minstar;
        this.maxstar = maxstar;
        this.starLimit = starLimit;
        this.isProtect = isProtect;
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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getMinstar() {
        return minstar;
    }

    public void setMinstar(Integer minstar) {
        this.minstar = minstar;
    }

    public Integer getMaxstar() {
        return maxstar;
    }

    public void setMaxstar(Integer maxstar) {
        this.maxstar = maxstar;
    }

    public Integer getStarLimit() {
        return starLimit;
    }

    public void setStarLimit(Integer starLimit) {
        this.starLimit = starLimit;
    }

    public Integer getIsProtect() {
        return isProtect;
    }

    public void setIsProtect(Integer isProtect) {
        this.isProtect = isProtect;
    }

}
