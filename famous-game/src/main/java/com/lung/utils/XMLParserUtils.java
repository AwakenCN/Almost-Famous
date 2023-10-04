package com.lung.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoyitao
 * @implSpec
 * @since 2023/9/25 - 15:19
 * @version 1.0
 */
public class XMLParserUtils {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "password";

        // 创建数据库连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 创建 JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 读取data目录下所有文件
        String dataFolderPath = "data";
        try {
            Files.walk(Paths.get(dataFolderPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            // 解析XML文件
                            parseAndStoreXML(file, jdbcTemplate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseAndStoreXML(Path filePath, JdbcTemplate jdbcTemplate) throws Exception {
        // 解析XML文件
        ClassPathResource resource = new ClassPathResource(filePath.toString());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(resource.getInputStream());

        // 解析XML数据并存储到数据库
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("item");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // 从element中解析数据
                int id = Integer.parseInt(element.getAttribute("id"));
                String name = element.getAttribute("name");
                // 解析其他字段...

                // 将数据存储到数据库
                String sql = "INSERT INTO mytable (id, name) VALUES (?, ?)";
                jdbcTemplate.update(sql, id, name);
            }
        }
    }

}
