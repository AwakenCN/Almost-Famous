package com.noseparte.robot.gm;

/**
 * @Auther: Noseparte
 * @Date: 2019/12/27 14:57
 * @Description: <p></p>
 */
public class GameMaster {

    public static String generateGameParams(String scene, String typeOrNum, String extend) {

        return scene + ";" +
                typeOrNum + ";" +
                extend;
    }
}
