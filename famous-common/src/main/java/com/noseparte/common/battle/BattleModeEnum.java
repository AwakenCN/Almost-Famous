package com.noseparte.common.battle;

public enum BattleModeEnum {

    LEISURE(1), RANK(2);

    private int value;

    BattleModeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BattleModeEnum parse(int value) {
        switch (value) {
            case 1:
                return LEISURE;
            case 2:
                return RANK;
            default:
                return null;
        }
    }

}
