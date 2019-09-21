package com.noseparte.common.resources;

/**
 *
 */
public class PackageConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /********** attribute ***********/
    private Integer id;

    private String icon;

    private String drop;

    private Integer maxPileNum;

    private Integer price;

    private Integer isAuto;

    /********** constructors ***********/
    public PackageConf() {

    }

    public PackageConf(Integer id, String icon, String drop, Integer maxPileNum, Integer price, Integer isAuto) {
        this.id = id;
        this.icon = icon;
        this.drop = drop;
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
