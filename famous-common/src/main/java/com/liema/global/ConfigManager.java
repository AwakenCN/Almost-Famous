package com.liema.global;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Noseparte
 * @date 2019/8/8 15:37
 * @Description
 */
@Slf4j
public class ConfigManager {

    private final static Map<String, Properties> PROP_NAMES = new HashMap<>();

    public static String getStringValue(String propName, String key) {
        Properties prop = ConfigManager.PROP_NAMES.get(propName);
        return prop.getProperty(key);
    }

    public static Integer getIntegerValue(String propName, String key) {
        Properties prop = ConfigManager.PROP_NAMES.get(propName);
        String value = prop.getProperty(key);
        return Integer.parseInt(value);
    }


}
