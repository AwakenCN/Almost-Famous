package com.noseparte.common.resources;

/**
 *
 */
public class GuideQuestConf implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

/********** attribute ***********/
    private Integer id;

    private Integer groupId;

    private Integer type;

    private Integer isSave;

    private String talk;

    private String content;

    private Integer doneType;

    private String Panel;

    private Integer role;

/********** constructors ***********/
    public GuideQuestConf() {

    }

    public GuideQuestConf(Integer id, Integer groupId, Integer type, Integer isSave, String talk, String content, Integer doneType, String Panel, Integer role) {
        this.id = id;
        this.groupId = groupId;
        this.type = type;
        this.isSave = isSave;
        this.talk = talk;
        this.content = content;
        this.doneType = doneType;
        this.Panel = Panel;
        this.role = role;
    }

/********** get/set ***********/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsSave() {
        return isSave;
    }

    public void setIsSave(Integer isSave) {
        this.isSave = isSave;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDoneType() {
        return doneType;
    }

    public void setDoneType(Integer doneType) {
        this.doneType = doneType;
    }

    public String getPanel() {
        return Panel;
    }

    public void setPanel(String Panel) {
        this.Panel = Panel;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}
