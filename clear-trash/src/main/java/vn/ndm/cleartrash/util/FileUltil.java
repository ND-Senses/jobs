package vn.ndm.cleartrash.util;

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
import java.util.stream.Collectors;

@Slf4j
public class FileUltil {

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

}
