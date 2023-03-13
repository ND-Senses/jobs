package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ManagerService {
    @Autowired
    DuplicateService duplicateService;
    @Autowired
    FileSplitterService fileSplitterService;
    @Autowired
    CleanService cleanService;

    @Autowired
    @Qualifier("cache")
    Map<String, List<String>> cmdCache;

    @Scheduled(cron = "0 * * ? * *")
    public void selectCmd() {
        log.info("#Init");
        try {
            for (Map.Entry<String, List<String>> entry : cmdCache.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                log.info("Key: {} Value: {}", key, value);
                if (key.contains("DELETE")) {
                    for (String v : value)
                        cleanService.deleteRecursive(new File(v));
                } else if (key.contains("INFO")) {
                    for (String v : value)
                        cleanService.getInfoFIle(v);
                } else if (key.contains("DUPLICATE")) {
                    for (String v : value)
                        duplicateService.duplicateFile(v);
                } else if (key.contains("CHF")) {
                    for (String v : value) {
                        String[] string = v.split(",");
                        cleanService.renameFileExtension(string[0], string[1]);
                    }
                } else if (key.contains("SPLITER")) {
                    for (String v : value)
                        fileSplitterService.searchFiles(v);
                } else {
                    log.info("Eó có gì .... lêu lêu");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
