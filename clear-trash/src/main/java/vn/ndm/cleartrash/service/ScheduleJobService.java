package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.cache.CmdCache;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ScheduleJobService {
    final DetectiveService service;
    final CmdCache cache;
    final Map<String, List<String>> cmdCache;

    @Autowired
    public ScheduleJobService(DetectiveService service, CmdCache cache, @Qualifier("cache") Map<String, List<String>> cmdCache) {
        this.service = service;
        this.cache = cache;
        this.cmdCache = cmdCache;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void selectCmd() {
        log.info("#Init");
        try {
            for (Map.Entry<String, List<String>> entry : cmdCache.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                log.info("Key: {} Value: {}", key, value);
                service.executeJob(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 30000)
    public void reloadJobConfigs() {
        Map<String, List<String>> listMap = cache.load();
        log.info("Size map: {}", listMap.size());
    }
}
