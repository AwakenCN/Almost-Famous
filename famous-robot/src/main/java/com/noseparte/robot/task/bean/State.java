package com.noseparte.robot.task.bean;

public enum State {

    PREPARED(1),

    RUNNING(2),

    STOPPED(3)
    ;

    private String code;
    private int value;

    State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
