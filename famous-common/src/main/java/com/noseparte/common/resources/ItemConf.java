package com.noseparte.common.resources;

/**
 *
 */
public class ItemConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String name;

    private String introduction;

    private Integer type;

    private String icon;

    private String drop;

    private String increment;

    private String specialDrop;

    private Integer maxPileNum;

    private Integer price;

    private Integer isAuto;

/********** constructors ***********/
    public ItemConf() {

    }

    public ItemConf(Integer id, String name, String introduction, Integer type, String icon, String drop, String increment, String specialDrop, Integer maxPileNum, Integer price, Integer isAuto) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.type = type;
        this.icon = icon;
        this.drop = drop;
        this.increment = increment;
        this.specialDrop = specialDrop;
        this.maxPileNum = maxPileNum;
        this.price = price;
        this.isAuto = isAuto;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getSpecialDrop() {
        return specialDrop;
    }

    public void setSpecialDrop(String specialDrop) {
        this.specialDrop = specialDrop;
    }

    public Integer getMaxPileNum() {
        return maxPileNum;
    }

    public void setMaxPileNum(Integer maxPileNum) {
        this.maxPileNum = maxPileNum;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }

}
