package com.lung.ai.astar;

import java.util.List;

public class PathPlanningExample {

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 0, 0},
                {0, 0, 0, 0, 0}
        };
        GridMap gridMap = new GridMap(grid, new GridPoint(0, 0), new GridPoint(4, 4));

        List<GridPoint> path = AStar.findPath(gridMap);
        if (path.isEmpty()) {
            System.out.println("无法找到路径");
        } else {
            System.out.println("找到路径：");
            for (GridPoint point : path) {
                System.out.println("(" + point.getRow() + ", " + point.getCol() + ")");
            }
        }
    }
}
