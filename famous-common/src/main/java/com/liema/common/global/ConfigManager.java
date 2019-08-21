package com.liema.common.global;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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


    public static void init(String propDir) {
        for (File file : new File(propDir).listFiles()) {
            if (file.getName().endsWith(".properties")) {
                loadProp(file);
            }
        }
    }

    private static void loadProp(File file) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            prop.load(fis);
            String propName = file.getName().substring(0, file.getName().indexOf(".properties"));
            PROP_NAMES.putIfAbsent(propName, prop);
        } catch (IOException e) {
            log.error("loadProp occur IOException, ", e);
        }
    }

    public static void loadGameData(String path) {

    }
}
