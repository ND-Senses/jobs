package vn.ndm.cleartrash.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.ndm.cleartrash.cache.CmdCache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JobScheduler {
    @Autowired
    CmdCache cache;

    @Autowired
    public JobScheduler(CmdCache cmdCache) {
        this.cache = cmdCache;
    }

    @Scheduled(fixedRate = 30000)
    public void reloadJobConfigs() throws IOException {
        Map<String, List<String>> listMap = cache.load();
        log.info("Size map: {}", listMap.size());
    }
}
