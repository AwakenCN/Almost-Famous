package com.lung.ai.astar;

public class GridPoint {
    private int row;
    private int col;

    public GridPoint(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
