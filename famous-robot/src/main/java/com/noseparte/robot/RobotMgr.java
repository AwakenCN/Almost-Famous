package com.noseparte.robot;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RobotMgr {
    private static RobotMgr instance = new RobotMgr();
    private static Map<Long, Robot> robotMap = new ConcurrentHashMap<>();

    private RobotMgr() {
    }

    public static RobotMgr getInstance() {
        return instance;
    }

    public Robot getRobot(long rid) {
        return robotMap.get(rid);
    }

    public void removeRobot(long rid) {
        robotMap.remove(rid);
    }

    public Map<Long, Robot> getRobotMap() {
        return robotMap;
    }

    public void addRobot(Robot robot) {
        if (robot.rid <= 0)
            return;
        robotMap.putIfAbsent(robot.rid, robot);
    }

    public int getLoginCount() {
        return robotMap.size();
    }
}
