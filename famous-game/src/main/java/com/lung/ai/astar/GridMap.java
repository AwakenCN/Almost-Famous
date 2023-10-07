package com.lung.ai.astar;

public class GridMap {

    private int[][] grid;
    private int numRows;
    private int numCols;
    private GridPoint start;
    private GridPoint goal;

    public GridMap(int[][] grid, GridPoint start, GridPoint goal) {
        this.grid = grid;
        this.numRows = grid.length;
        this.numCols = grid[0].length;
        this.start = start;
        this.goal = goal;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public GridPoint getStart() {
        return start;
    }

    public GridPoint getGoal() {
        return goal;
    }

    public boolean isBlocked(int row, int col) {
        return grid[row][col] == 1;
    }
}
