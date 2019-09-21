package com.noseparte.common.resources;

/**
 *
 */
public class GlobalVariableConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /********** attribute ***********/
    private Integer id;

    private String value;

    /********** constructors ***********/
    public GlobalVariableConf() {

    }

    public GlobalVariableConf(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    /********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GlobalVariableConf{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
