package com.noseparte.common.resources;

/**
 *
 */
public class CardAttrConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private Integer strenth;

    private Integer intAndAgi;

/********** constructors ***********/
    public CardAttrConf() {

    }

    public CardAttrConf(Integer id, Integer strenth, Integer intAndAgi) {
        this.id = id;
        this.strenth = strenth;
        this.intAndAgi = intAndAgi;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStrenth() {
        return strenth;
    }

    public void setStrenth(Integer strenth) {
        this.strenth = strenth;
    }

    public Integer getIntAndAgi() {
        return intAndAgi;
    }

    public void setIntAndAgi(Integer intAndAgi) {
        this.intAndAgi = intAndAgi;
    }

}
