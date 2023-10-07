package com.lung.ai.astar;

import java.util.*;

public class AStar {

    public static List<GridPoint> findPath(GridMap gridMap) {
        // 创建起始节点和目标节点
        GridPoint start = gridMap.getStart();
        GridPoint goal = gridMap.getGoal();

        // 创建open集合和closed集合
        List<GridPoint> openSet = new ArrayList<>();
        Set<GridPoint> closedSet = new HashSet<>();

        // 将起始节点加入open集合
        openSet.add(start);

        // 创建父节点锁定表和G值表
        Map<GridPoint, GridPoint> parentMap = new HashMap<>();
        Map<GridPoint, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        // 创建起始节点估计F值表
        Map<GridPoint, Double> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal)); // 初始节点的F值等于启发函数值

        while (!openSet.isEmpty()) {
            // 从open集合中获取F值最小的节点
            GridPoint current = getLowestFScore(openSet, fScore);

            // 如果当前节点为目标节点，停止搜索并返回路径
            if (current.equals(goal)) {
                return reconstructPath(parentMap, current);
            }

            // 将当前节点从open集合中移除，并加入closed集合
            openSet.remove(current);
            closedSet.add(current);

            // 获取当前节点的相邻节点
            List<GridPoint> neighbors = getNeighbors(current, gridMap);
            for (GridPoint neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue; // 跳过已经处理过的邻居节点
                }

                double tentativeGScore = gScore.get(current) + distance(current, neighbor);
                if (!openSet.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    // 更新父节点和G值
                    parentMap.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);

                    // 计算估计F值
                    double neighborFScore = tentativeGScore + heuristic(neighbor, goal);
                    fScore.put(neighbor, neighborFScore);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor); // 将邻居节点加入open集合
                    }
                }
            }
        }

        return new ArrayList<>(); // 未找到路径
    }

    // 通过父节点锁定表从目标节点到起始节点重构路径
    private static List<GridPoint> reconstructPath(Map<GridPoint, GridPoint> parentMap, GridPoint current) {
        List<GridPoint> path = new ArrayList<>();
        path.add(current);

        while (parentMap.containsKey(current)) {
            current = parentMap.get(current);
            path.add(0, current);
        }

        return path;
    }

    // 获取F值最小的节点
    private static GridPoint getLowestFScore(List<GridPoint> openSet, Map<GridPoint, Double> fScore) {
        GridPoint lowestFScoreNode = openSet.get(0);

        for (int i = 1; i < openSet.size(); i++) {
            if (fScore.get(openSet.get(i)) < fScore.get(lowestFScoreNode)) {
                lowestFScoreNode = openSet.get(i);
            }
        }

        return lowestFScoreNode;
    }

    // 获取当前节点的相邻节点
    private static List<GridPoint> getNeighbors(GridPoint current, GridMap gridMap) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向
        List<GridPoint> neighbors = new ArrayList<>();

        for (int[] direction : directions) {
            int newRow = current.getRow() + direction[0];
            int newCol = current.getCol() + direction[1];

            if (newRow >= 0 && newRow < gridMap.getNumRows() && newCol >= 0 && newCol < gridMap.getNumCols()) {
                if (!gridMap.isBlocked(newRow, newCol)) {
                    neighbors.add(new GridPoint(newRow, newCol));
                }
            }
        }

        return neighbors;
    }

    // 计算两个节点之间的曼哈顿距离
    private static double distance(GridPoint p1, GridPoint p2) {
        return Math.abs(p1.getRow() - p2.getRow()) + Math.abs(p1.getCol() - p2.getCol());
    }

    // 启发函数（这里使用曼哈顿距离作为启发函数）
    private static double heuristic(GridPoint p1, GridPoint p2) {
        return distance(p1, p2);
    }
}
