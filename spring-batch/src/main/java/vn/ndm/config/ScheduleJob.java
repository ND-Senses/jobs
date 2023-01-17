package vn.ndm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
public class ScheduleJob {

    private final Job job;
    private final JobLauncher jobLauncher;

    public ScheduleJob(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(fixedDelayString = "30000")
    public void perform() throws Exception {
        log.info("Job Started at :" + new Date());
        JobParameters param = new JobParametersBuilder().addString(job.getName(), String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, param);
        log.info("Job finished with status :" + execution.getStatus());
    }

}
