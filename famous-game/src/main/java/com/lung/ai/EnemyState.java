package com.lung.ai;

public abstract class EnemyState {
    public abstract void enter();
    public abstract void update();
    public abstract void exit();
    public abstract EnemyState getNextState();
}
