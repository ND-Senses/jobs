package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CleanService {
    private static final ConcurrentMap<String, String> mapPathCache = new ConcurrentHashMap<>();

    @Scheduled(cron = "0 * * ? * *")
    public void init() {
        log.info("#Init");
        try {
            for (Map.Entry<String, String> entry : mapPathCache.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                log.info("Key: {} Value: {}", key, value);
                if (key.contains("DEL")) {
                    deleteRecursive(new File(value));
                } else if (key.contains("INFO")) {
                    getInfoFIle(value);
                } else if (key.contains("DUP")) {
                    duplicateFile(value);
                } else if (key.contains("CHF")) {
                    String[] string = value.split(",");
                    renameFileExtension(string[0], string[1]);
                } else {
                    log.info("Eó có gì .... lêu lêu");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInfoFIle(String path) {
        List<String> strings = getAllPath(path);
        for (String pathFile : strings) {
            File file = new File(pathFile);
            log.info("File name {} size {}", file.getName(), file.length());
        }
    }

    @PostConstruct //30s
    @Scheduled(cron = "0/30 * * ? * *")
    public void loadPahtFile() {
        ConcurrentMap<String, String> mapNew = readPropertiesFileToMap();
        // add key remove
        if (!mapNew.isEmpty()) {
            mapPathCache.clear();
            // put new map
            mapPathCache.putAll(mapNew);
        }
        log.info("#MapPath size: {}", mapPathCache.size());
    }

    public static ConcurrentMap<String, String> readPropertiesFileToMap() {
        Properties prop = new Properties();
        ConcurrentHashMap<String, String> stringMap = new ConcurrentHashMap<>();
        try (InputStream fis = ClassLoader.getSystemResourceAsStream("path.properties")) {
            prop.load(fis);
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

    public void deleteRecursive(File path) {
        log.info("Cleaning out folder: {}", path.toString());
        try {
            for (File file : Objects.requireNonNull(path.listFiles())) {
                if (file.isDirectory()) {
                    deleteRecursive(file);
                    Path p = Paths.get(file.getPath());
                    log.info("Delete folder {} status: {}", p, Files.deleteIfExists(p));
                } else {
                    Path p = Paths.get(file.getPath());
                    log.info("Delete file {} status: {}", p, Files.deleteIfExists(p));
                }
            }
        } catch (Exception e) {
            log.info("#Error delete file: {}", e.getMessage());
        }
    }

    //get list file folder
    public List<String> getAllPath(String path) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File f = new File(path);
            for (File file : Objects.requireNonNull(f.listFiles())) {
                if (file.isDirectory()) {
                    log.info("get folder: {}", file.getAbsolutePath());
                    list.add(file.getAbsolutePath());
                    getAllPath(file.getAbsolutePath());
                } else {
                    log.info("get name: {}", file.getName());
                    list.add(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    /**
     * check duplicate:
     * enCode
     * used FileInputStream read()
     *
     * @param path
     * @throws IOException
     */
    public void duplicateFile(String path) throws IOException {
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (int x = 0; x < fileList.length; x++) {
                for (int y = x + 1; y < fileList.length; y++) {
                    if (fileList[x].length() == fileList[y].length() && compareFiles(fileList[x], fileList[y])) {
                        Path p = Paths.get(String.valueOf(fileList[x]));
                        Files.deleteIfExists(p);
                    }
                }
            }
        }
    }

    public static boolean compareFiles(File x, File y) {
        try (FileInputStream xs = new FileInputStream(x);
             FileInputStream ys = new FileInputStream(y)) {
            log.info("Compare: " + x + " vs " + y);
            boolean result = true;
            while (result) {
                int xb = xs.read();
                int yb = ys.read();
                if (xs.read() != ys.read()) {
                    result = false;
                }
                if (xb == -1 || yb == -1)
                    result = false;
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void renameFileExtension(String source, String newExtension) {
        String target;
        File file1 = new File(source);
        File[] files = file1.listFiles();
        assert files != null;
        log.info("size data {}", files.length);
        for (File file : Objects.requireNonNull(files)) {
            String currentExtension = getFileExtension(file.getAbsolutePath());
            if (currentExtension.contains("jfif")) {
                log.info("file: {}", file.getAbsoluteFile());
                target = file.getAbsolutePath().replaceFirst(Pattern.quote("." + currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));
                boolean checkExits = new File(file.getAbsolutePath()).renameTo(new File(target));
                if (checkExits) {
                    log.info("Succsess: {}", target);
                }
            }
        }
    }

    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }

    public static void findDuplicateFiles(Map<String, List<String>> filesList, File directory) throws
            NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-512");
        File[] files = directory.listFiles();
        for (File dirChild : Objects.requireNonNull(files)) {
            // Iterate all file sub directories recursively
            if (dirChild.isDirectory()) {
                findDuplicateFiles(filesList, dirChild);
            } else {
                try (InputStream is = new FileInputStream(dirChild)) {
                    // Read file as bytes
                    byte[] buffer = new byte[1000];
                    while (is.read(buffer) > 0) {
                        String uniqueFileHash = new BigInteger(1, messageDigest.digest(buffer)).toString(16);
                        List<String> identicalList = filesList.get(uniqueFileHash);
                        if (identicalList == null) {
                            identicalList = new LinkedList<>();
                        }
                        // Add path to list
                        identicalList.add(dirChild.getAbsolutePath());
                        // push updated list to Hash table
                        filesList.put(uniqueFileHash, identicalList);
                    }
                } catch (IOException e) {
                    log.info("cannot read file " + dirChild.getAbsolutePath(), e);
                }
            }
        }
    }
}
