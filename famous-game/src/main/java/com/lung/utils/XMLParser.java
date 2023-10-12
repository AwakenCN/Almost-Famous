package com.lung.utils;

import com.lung.model.HeroConfig;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoyitao
 * @implSpec
 * @since 2023/9/25 - 15:19
 * @version 1.0
 */
public class XMLParser {

    public static void main(String[] args) {
        // 解析XML文件
        try {
            // 读取 XML 文件
            ClassPathResource resource = new ClassPathResource("data/hero.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());

            // 创建 Map 存储英雄信息
            Map<Integer, HeroConfig> heroMap = new HashMap<>();

            // 解析 <group id="hero"> 节点
            Element heroGroup = (Element) document.getElementsByTagName("group").item(0);
            NodeList heroList = heroGroup.getElementsByTagName("item");
            for (int i = 0; i < heroList.getLength(); i++) {
                Node heroNode = heroList.item(i);
                if (heroNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element heroElement = (Element) heroNode;
                    int id = Integer.parseInt(heroElement.getAttribute("id"));
                    String name = heroElement.getAttribute("name");
                    int attack = Integer.parseInt(heroElement.getAttribute("attack"));
                    int defend = Integer.parseInt(heroElement.getAttribute("defend"));
                    int speed = Integer.parseInt(heroElement.getAttribute("speed"));
                    int intellect = Integer.parseInt(heroElement.getAttribute("intellect"));
                    int type = Integer.parseInt(heroElement.getAttribute("type"));

                    HeroConfig heroConfig = new HeroConfig(id, name, attack, defend, speed, intellect, type);
                    heroMap.put(id, heroConfig);
                }
            }

            // 解析 <group id="hero_type"> 节点
            Element heroTypeGroup = (Element) document.getElementsByTagName("group").item(1);
            NodeList heroTypeList = heroTypeGroup.getElementsByTagName("item");
            for (int i = 0; i < heroTypeList.getLength(); i++) {
                Node heroTypeNode = heroTypeList.item(i);
                if (heroTypeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element heroTypeElement = (Element) heroTypeNode;
                    int id = Integer.parseInt(heroTypeElement.getAttribute("id"));
                    String name = heroTypeElement.getAttribute("name");

                    // 将英雄类型信息添加到对应的英雄对象中
                    HeroConfig heroConfig = heroMap.get(id);
                    if (heroConfig != null) {
                        heroConfig.setType(name);
                    }
                }
            }

            // 打印英雄信息
            for (Map.Entry<Integer, HeroConfig> entry : heroMap.entrySet()) {
                HeroConfig heroConfig = entry.getValue();
                System.out.println(heroConfig.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
