package vn.ndm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.ndm.tasklet.bhtt.TaskletCreateFile;
import vn.ndm.tasklet.ftp.DownloadFileFTP;
import vn.ndm.tasklet.ftp.UpLoadFileFTP;
import vn.ndm.tasklet.vasp.TaskletImportOCSSP;


@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private static final String MANAGE_JOB1 = "manageJob1";
    private static final String STEP_1 = "step1";
    private static final String STEP_2 = "step2";
    private static final String STEP_3 = "step3";
    private static final String STEP_4 = "step4";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExecutionListener listener;
    private final TaskletImportOCSSP importOCSSP;
    private final TaskletCreateFile createFile;
    private final DownloadFileFTP downloadFileFTP;
    private final UpLoadFileFTP upLoadFileFTP;


    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              @Qualifier("listener") JobExecutionListener listener,
                              @Qualifier("tasklet-import-ocs") TaskletImportOCSSP importOCSSP,
                              @Qualifier("tasklet-create-file") TaskletCreateFile createFile,
                              @Qualifier("download-file-ftp") DownloadFileFTP downloadFileFTP,
                              @Qualifier("upload-file-ftp") UpLoadFileFTP upLoadFileFTP) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.listener = listener;
        this.importOCSSP = importOCSSP;
        this.createFile = createFile;
        this.downloadFileFTP = downloadFileFTP;
        this.upLoadFileFTP = upLoadFileFTP;
    }

    @Bean(name = "manageJob1")
    public Job manageJob1() {
        return jobBuilderFactory.get(MANAGE_JOB1)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step4()).next(step3()).build();
    }

    @Bean
    public Step step1() {
        log.info(STEP_1);
        return stepBuilderFactory.get(STEP_1)
                .tasklet(createFile)
                .build();
    }

    @Bean
    public Step step2() {
        log.info(STEP_2);
        return stepBuilderFactory.get(STEP_2)
                .tasklet(importOCSSP)
                .build();
    }

    @Bean
    public Step step3() {
        log.info(STEP_3);
        return stepBuilderFactory.get(STEP_3)
                .tasklet(downloadFileFTP)
                .build();
    }

    @Bean
    public Step step4() {
        log.info(STEP_4);
        return stepBuilderFactory.get(STEP_4)
                .tasklet(upLoadFileFTP)
                .build();
    }
}
