package vn.ndm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
public class ScheduleJob {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public ScheduleJob(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Scheduled(fixedDelayString = "30000")
    public void perform() throws Exception {
        for (String jobName : jobRegistry.getJobNames()) {
            log.info("Job Started at :" + new Date());
            JobParameters param = new JobParametersBuilder().addString(jobName, String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(jobRegistry.getJob(jobName), param);
            log.info("Job finished with status :" + execution.getStatus());
        }
    }
}
