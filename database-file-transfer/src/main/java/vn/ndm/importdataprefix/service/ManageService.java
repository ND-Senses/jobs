package vn.ndm.importdataprefix.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import vn.ndm.importdataprefix.service.impl.JobImport;

import javax.annotation.PostConstruct;

@Service
public class ManageService {
    private final ThreadPoolTaskExecutor service;
    private final JobImport run;

    public ManageService(ThreadPoolTaskExecutor service, JobImport run) {
        this.service = service;
        this.run = run;
    }

    @PostConstruct
    public void init() {
        service.execute(run);
    }
}
