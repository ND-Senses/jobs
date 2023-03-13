package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class FileUltil {
    public static void main(String[] args) {
        System.out.println(readYamlFile("config/job-config.yml"));
    }

    public static ConcurrentMap<String, List<String>> readYamlFile(String filePath) {
        ConcurrentMap<String, List<String>> jobs = new ConcurrentHashMap<>();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> data = yaml.load(inputStream);
            List<Map<String, Object>> jobsList = data.get("jobs");
            for (Map<String, Object> jobMap : jobsList) {
                String name = (String) jobMap.get("name");
                Object paths = jobMap.get("paths");
                jobs.put(name, (List<String>) paths);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;
    }

    // check folder exists
    public static void checkFolderisExists(String path) {
        try {
            File p = new File(path);
            boolean isDirectoryCreated = p.exists();
            if (!isDirectoryCreated) {
                log.info("Folder does not exists!  --> Created");
                isDirectoryCreated = p.mkdirs();
                if (isDirectoryCreated) {
                    log.info("Folder create succ!");
                }
            } else {
                log.info("Folder is exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Folder create error!");

        }
    }

    public static ConcurrentMap<String, String> readPropertiesFileToMap(String path) {
        Properties prop = new Properties();
        ConcurrentHashMap<String, String> stringMap = new ConcurrentHashMap<>();
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            Enumeration<Object> enuKeys = prop.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = prop.getProperty(key);
                stringMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringMap;
    }
}
