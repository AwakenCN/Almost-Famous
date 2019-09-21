package com.noseparte.common.resources;

/**
 *
 */
public class MapConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private String name;

    private String position1;

    private String position2;

    private String map;

    private String initSkill1;

    private String initSkill2;

    private String initSkill3;

    private String initSkill4;

/********** constructors ***********/
    public MapConf() {

    }

    public MapConf(Integer id, String name, String position1, String position2, String map, String initSkill1, String initSkill2, String initSkill3, String initSkill4) {
        this.id = id;
        this.name = name;
        this.position1 = position1;
        this.position2 = position2;
        this.map = map;
        this.initSkill1 = initSkill1;
        this.initSkill2 = initSkill2;
        this.initSkill3 = initSkill3;
        this.initSkill4 = initSkill4;
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

    public String getPosition1() {
        return position1;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public String getPosition2() {
        return position2;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getInitSkill1() {
        return initSkill1;
    }

    public void setInitSkill1(String initSkill1) {
        this.initSkill1 = initSkill1;
    }

    public String getInitSkill2() {
        return initSkill2;
    }

    public void setInitSkill2(String initSkill2) {
        this.initSkill2 = initSkill2;
    }

    public String getInitSkill3() {
        return initSkill3;
    }

    public void setInitSkill3(String initSkill3) {
        this.initSkill3 = initSkill3;
    }

    public String getInitSkill4() {
        return initSkill4;
    }

    public void setInitSkill4(String initSkill4) {
        this.initSkill4 = initSkill4;
    }

}
