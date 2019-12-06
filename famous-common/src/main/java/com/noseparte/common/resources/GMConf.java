package com.noseparte.common.resources;

/**
 *
 */
public class GMConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String name;

    private String desc;

    private String val1Name;

    private Integer val1Type;

    private String val2Name;

    private Integer val2Type;

    private String val3Name;

    private Integer val3Type;

/********** constructors ***********/
    public GMConf() {

    }

    public GMConf(Integer id, String name, String desc, String val1Name, Integer val1Type, String val2Name, Integer val2Type, String val3Name, Integer val3Type) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.val1Name = val1Name;
        this.val1Type = val1Type;
        this.val2Name = val2Name;
        this.val2Type = val2Type;
        this.val3Name = val3Name;
        this.val3Type = val3Type;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVal1Name() {
        return val1Name;
    }

    public void setVal1Name(String val1Name) {
        this.val1Name = val1Name;
    }

    public Integer getVal1Type() {
        return val1Type;
    }

    public void setVal1Type(Integer val1Type) {
        this.val1Type = val1Type;
    }

    public String getVal2Name() {
        return val2Name;
    }

    public void setVal2Name(String val2Name) {
        this.val2Name = val2Name;
    }

    public Integer getVal2Type() {
        return val2Type;
    }

    public void setVal2Type(Integer val2Type) {
        this.val2Type = val2Type;
    }

    public String getVal3Name() {
        return val3Name;
    }

    public void setVal3Name(String val3Name) {
        this.val3Name = val3Name;
    }

    public Integer getVal3Type() {
        return val3Type;
    }

    public void setVal3Type(Integer val3Type) {
        this.val3Type = val3Type;
    }

}
