package vn.ndm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ndm.tasklet.ftp.DownloadFileFTP;
import vn.ndm.tasklet.ftp.UpLoadFileFTP;

@Slf4j
@Configuration
@EnableBatchProcessing
public class JobFtpImpl implements JobFactory {
    private final DownloadFileFTP downloadFileFTP;
    private final UpLoadFileFTP upLoadFileFTP;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExecutionListener listener;

    public JobFtpImpl(DownloadFileFTP downloadFileFTP,
                      UpLoadFileFTP upLoadFileFTP,
                      JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory,
                      JobExecutionListener listener) {
        this.downloadFileFTP = downloadFileFTP;
        this.upLoadFileFTP = upLoadFileFTP;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.listener = listener;
    }

    @Override
    public Job createJob() {
        return jobBuilderFactory.get(getJobName())
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1()).next(step2()).build();
    }

    @Override
    public String getJobName() {
        return "JobFTP";
    }

    @Bean
    public Step step1() {
        log.info("step1");
        return stepBuilderFactory.get("step1")
                .tasklet(downloadFileFTP)
                .build();
    }

    @Bean
    public Step step2() {
        log.info("step2");
        return stepBuilderFactory.get("step2")
                .tasklet(upLoadFileFTP)
                .build();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
