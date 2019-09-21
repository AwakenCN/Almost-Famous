package com.noseparte.common.resources;

/**
 *
 */
public class MaskWordConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private String id;

    private String chinese;

/********** constructors ***********/
    public MaskWordConf() {

    }

    public MaskWordConf(String id, String chinese) {
        this.id = id;
        this.chinese = chinese;
    }

/********** get/set ***********/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

}
