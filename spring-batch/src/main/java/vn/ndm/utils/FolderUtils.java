package vn.ndm.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class FolderUtils {
    // get info file date
    public static BasicFileAttributes getFileTime(File file) {
        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attrs;
    }

    //get list file folder
    public static List<String> readFolder(String path) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File folder = new File(path);
            File[] lf = folder.listFiles();
            if (lf == null || lf.length == 0) {
                return list;
            } else {
                for (File f : lf) {
                    if (f.isDirectory()) {
                        readFolder(f.getPath());
                    }
                    list.add(f.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // get file java 8
    public static List<String> getAllFiles(String directory) {
        List<String> fileList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> fileList.add(file.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    // get k-v file properties
    public Map<String, String> getHashMap(Properties properties) {
        HashMap<String, String> map = new HashMap<>();
        Enumeration<?> enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            map.put(key, value);
        }
        return map;
    }

    // get obj properties
    public Properties getPathProperties(String path) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(path);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties getFilePathToSave() {
        Properties prop = new Properties();
        try (InputStream inputStream = FolderUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static void checkExists(File p) {
        try {
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

    public static void main(String[] args) {
        String path = "C:/Users/admin/Desktop/1090-901";
        for (String a : getAllFiles(path)) {
            System.out.println(a);
        }
    }
}

