package com.noseparte.common.global;

import com.noseparte.common.resources.*;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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

    public static Object convertor(String filedValue, String filedType) {
        switch (filedType) {
            case "byte":
                return Byte.parseByte(filedValue);
            case "long":
                return Long.parseLong(filedValue);
            case "int":
                return Integer.parseInt(filedValue);
            case "double":
                return Double.parseDouble(filedValue);
            case "short":
                return Short.parseShort(filedValue);
            case "float":
                return Float.parseFloat(filedValue);
            case "bool":
                return Boolean.parseBoolean(filedValue);
            default:
                break;
        }
        return filedValue;
    }

    public static Map<Integer, Object> loadTxt(File file, Element resource) {
        String packName = resource.getAttribute("packName");
        String className = resource.getAttribute("className");
        Map<Integer, Object> dataMap = new HashMap<>();// 保存配表数据
        int idx = 0;
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            String[] fileds = null, types = null;
            while ((line = br.readLine()) != null) {
                if (idx == 1) {
                    fileds = line.split("\\\t");
                } else if (idx == 2) {
                    types = line.split("\\\t");
                } else if (idx >= 3) {
                    String[] values = line.split("\\\t");
                    Object newInstance = Class.forName(packName + "." + className).newInstance();
                    for (int i = 0; i < values.length; i++) {
                        String value = values[i];
                        if (null == value || "".equals(value)) {
                            continue;
                        }
                        String filed = fileds[i];
                        String type = types[i];
                        // 赋值
                        PropertyDescriptor pd = new PropertyDescriptor(filed, newInstance.getClass());
                        Method setMethod = pd.getWriteMethod();
                        setMethod.invoke(newInstance, convertor(value, type));
                    }
                    dataMap.put(Integer.parseInt(values[0]), newInstance);
                }
                idx++;
            }
        } catch (Exception e) {
            log.error(file.getName() + "配表，行号 {}", (idx + 1), e);
        }

        log.info("游戏加载配表 {}, 加载{}行", file.getName(), (idx + 1));

        return dataMap;
    }

    public static List<Element> parseConfiguration(File file) {
        if (log.isDebugEnabled()) {
            log.debug("策划配置文件路径：{}", file.getPath());
        }
        List<Element> elements = new LinkedList<>();
        try {
            final Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(file);
            Element rootElement = doc.getDocumentElement();
            NodeList resourcesNodes = rootElement.getElementsByTagName("Resource");
            for (int i = 0; i < resourcesNodes.getLength(); ++i) {
                Node node = resourcesNodes.item(i);
                if (Node.ELEMENT_NODE != node.getNodeType()) {
                    continue;
                }
                Element element = (Element) node;
                String n = element.getNodeName();
                if ("Resource".equals(n)) {
                    elements.add(element);
                }
            }
        } catch (Exception e) {
            log.error("game data xml 解析失败", e);
        }
        return elements;
    }

    public static void loadGameData(String gamedataDir) {
        // parse xml
        List<Element> resources = parseConfiguration(new File(gamedataDir + "/resource.xml"));

        try {
            for (Element resource : resources) {
                String dataTable = resource.getAttribute("dataTable");
                String mapName = resource.getAttribute("mapName");
                File txtFile = new File(gamedataDir + "/data/" + dataTable);
                Map<Integer, Object> map = loadTxt(txtFile, resource);

                Field f = ConfigManager.class.getDeclaredField(mapName);
                f.setAccessible(true);
                f.set(ConfigManager.class, map);
            }
        } catch (Exception e) {
            log.error("game data 加载失败", e);
        }
    }


    /**
     * 卡牌表
     */
    public static Map<Integer, CardConf> cardConfMap = new HashMap<>();
    /**
     * 全局变量表
     */
    public static Map<Integer, GlobalVariableConf> globalVariableConfMap = new HashMap<>();
    /**
     * 职业表
     */
    public static Map<Integer, OccupationConf> occupationConfMap = new HashMap<>();
    /**
     * 职业初始化表
     */
    public static Map<Integer, SchoolInitConf> schoolInitConfMap = new HashMap<>();
    /**
     * 关卡表
     */
    public static Map<Integer, ChapterConf> chapterConfMap = new HashMap<>();
    /**
     * 道具表
     */
    public static Map<Integer, ItemConf> itemConfMap = new HashMap<>();
    /**
     * 掉落表
     */
    public static Map<Integer, DropItemConf> dropItemConfMap = new HashMap<>();
    /**
     * 礼包表
     */
    public static Map<Integer, PackageConf> packageConfMap = new HashMap<>();
    /**
     * 任务表
     */
    public static Map<Integer, MissionConf> missionConfMap = new HashMap<>();
    /**
     * 段位表
     */
    public static Map<Integer, BattleRankConf> battleRankConfMap = new HashMap<>();
    /**
     * 地图表
     */
    public static Map<Integer, MapConf> mapConfMap = new HashMap<>();
    /**
     * 敏感词表
     */
    public static Map<Integer, MaskWordConf> maskWordConfMap = new HashMap<>();

}
