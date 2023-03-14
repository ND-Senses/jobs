package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.itf.JobHandler;
import vn.ndm.cleartrash.service.impl.CleanService;
import vn.ndm.cleartrash.service.impl.DuplicateService;
import vn.ndm.cleartrash.service.impl.FileSplitterService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DetectiveService {
    final DuplicateService duplicateService;
    final FileSplitterService fileSplitterService;
    final CleanService cleanService;
    private final Map<String, JobHandler> jobHandlers = new HashMap<>();

    @Autowired
    public DetectiveService(DuplicateService duplicateService, FileSplitterService fileSplitterService, CleanService cleanService) {
        this.duplicateService = duplicateService;
        this.fileSplitterService = fileSplitterService;
        this.cleanService = cleanService;
    }

    @PostConstruct
    public void initDetectiveService() {
        // Register job handlers
        jobHandlers.put("DELETE", cleanService);
        jobHandlers.put("DUPLICATE", duplicateService);
        jobHandlers.put("SPLITER", fileSplitterService);
        // Add more job handlers here...
    }

    public void executeJob(String cmd, List<String> params) {
        log.info("jobHandlers: {}", jobHandlers.size());
        JobHandler jobHandler = jobHandlers.get(cmd);
        if (jobHandler != null) {
            jobHandler.execute(params);
        } else {
            log.info("Unsupported command: {}", cmd);
        }
    }
}
