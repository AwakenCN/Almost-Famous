package com.noseparte.common.battle.server;

public enum ConnState {

	CLIENT_CLOSE(1), SERVER_CLOSE(2), CLOSE_PROTOCOL_ERROR(3), CLOSE_EXCEPTION(4), CLOSE_NOT_LOGIN(5), CLOSE_HEARTBEAT_EXPIRE(6);

    private int state;

    ConnState(int state) {
        this.state = state;
    }

    public int value() {
        return this.state;
    }

}
