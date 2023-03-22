package vn.ndm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ndm.service.impl.JobFtpImpl;

import javax.annotation.PostConstruct;


@Slf4j
@Service
public class BatchConfiguration {

    private final JobFtpImpl jobFtpImpl;
    private final JobRegistry jobRegistry;

    @Autowired
    public BatchConfiguration(JobFtpImpl jobFtpImpl, JobRegistry jobRegistry) {
        this.jobFtpImpl = jobFtpImpl;
        this.jobRegistry = jobRegistry;
    }

    @PostConstruct
    public void setJobRegistry() throws DuplicateJobException {
        jobRegistry.register(jobFtpImpl);
    }
}
