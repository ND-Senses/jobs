package vn.ndm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ndm.service.impl.JobExportFileImpl;
import vn.ndm.service.impl.JobFtpImpl;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ManagerJob {

    private final JobFtpImpl jobFtpImpl;
    private final JobExportFileImpl exportFile;
    private final JobRegistry jobRegistry;

    @Autowired
    public ManagerJob(JobFtpImpl jobFtpImpl, JobExportFileImpl exportFile, JobRegistry jobRegistry) {
        this.jobFtpImpl = jobFtpImpl;
        this.exportFile = exportFile;
        this.jobRegistry = jobRegistry;
    }

    @PostConstruct
    public void setJobRegistry() throws DuplicateJobException {
        jobRegistry.register(jobFtpImpl);
        jobRegistry.register(exportFile);
    }
}
