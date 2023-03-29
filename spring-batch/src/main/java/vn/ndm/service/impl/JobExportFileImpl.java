package vn.ndm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ndm.tasklet.job.ExportFile;

@Slf4j
@Configuration
@EnableBatchProcessing
public class JobExportFileImpl implements JobFactory {
    private final ExportFile exportFile;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExecutionListener listener;

    public JobExportFileImpl(ExportFile exportFile,
                             JobBuilderFactory jobBuilderFactory,
                             StepBuilderFactory stepBuilderFactory,
                             JobExecutionListener listener) {
        this.exportFile = exportFile;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.listener = listener;
    }

    @Bean
    public Job jobExport() {
        return jobBuilderFactory.get(getJobName())
                .listener(listener)
                .start(step1()).build();
    }

    @Override
    public Job createJob() {
        return jobExport();
    }

    @Override
    public String getJobName() {
        return "JobExport";
    }

    @Bean
    public Step step1() {
        log.info("step1");
        return stepBuilderFactory.get("step1")
                .tasklet(exportFile)
                .build();
    }
}
