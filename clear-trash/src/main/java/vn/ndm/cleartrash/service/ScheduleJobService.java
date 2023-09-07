package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.config.PathsConfig;

import java.util.List;

@Slf4j
@Service
public class ScheduleJobService {
    final DetectiveService service;
    final PathsConfig pathsConfig;

    @Autowired
    public ScheduleJobService(DetectiveService service, PathsConfig pathsConfig) {
        this.service = service;
        this.pathsConfig = pathsConfig;
    }

    @Scheduled(cron = "${job.clear-time}")
    public void initJob() {
        String commnad = pathsConfig.getCommand();
        List<String> paths = pathsConfig.getPaths();
        service.executeJob(commnad, paths);
    }
}
