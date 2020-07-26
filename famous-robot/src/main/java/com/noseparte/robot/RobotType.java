package com.noseparte.robot;

import org.springframework.data.annotation.Immutable;

/**
 * @author Noseparte
 * @date 2019/9/18 10:21
 * @Description
 */
@Immutable
abstract class RobotType {

    final static int ROLE = 1;
    final static int SCHOOL = 2;
    final static int CARD_PACKAGE = 3;
    final static int MISSION = 4;
    final static int CHAPTER = 5;
    final static int SIGN = 6;
    final static int BAG = 7;
    final static int BATTLE = 8;

    final static String SINGEL = "single";
    final static String CLUSTER = "cluster";

}
