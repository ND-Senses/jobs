package vn.ndm.config;

import vn.ndm.listener.JobCompletionJobExecutionListener;
import vn.ndm.tasklet.eoffice.ExportFile;
import vn.ndm.tasklet.vasp.TaskletFileImportVasp;
import vn.ndm.tasklet.vasp.TaskletFileImportVasp2;
import vn.ndm.tasklet.vasp.TaskletImportOCSSP;
import vn.ndm.tasklet.bhtt.TaskletCreateFile;
import vn.ndm.tasklet.bhtt.TaskletImportBHTT;
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


@Slf4j
@EnableScheduling
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private static final String MANAGE_JOB1 = "manageJob1";
    private static final String STEP_ONE = "step1";
    private static final String STEP_TWO = "step2";
    private static final String STEP_THREE = "step3";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = "manageJob1")
    public Job manageJob1(@Qualifier(STEP_THREE) Step step3) {
        return jobBuilderFactory.get(MANAGE_JOB1)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener())
                .start(step3).build();
    }

    @Bean
    public Step step1() {
        log.info(STEP_ONE);
        return stepBuilderFactory.get(STEP_ONE)
                .tasklet(taskletCreateFile())
                .build();
    }

    @Bean
    public Step step2() {
        log.info(STEP_TWO);
        return stepBuilderFactory.get(STEP_TWO)
                .tasklet(taskletImportOCSSP())
                .build();
    }

    @Bean
    public Step step3() {
        log.info(STEP_THREE);
        return stepBuilderFactory.get(STEP_THREE)
                .tasklet(exportFile())
                .build();
    }


    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionJobExecutionListener();
    }

    @Bean
    public TaskletImportOCSSP taskletImportOCSSP() {
        return new TaskletImportOCSSP();
    }

    @Bean
    public TaskletCreateFile taskletCreateFile() {
        return new TaskletCreateFile();
    }

    @Bean
    public TaskletImportBHTT taskletImportBHTT() {
        return new TaskletImportBHTT();
    }

    @Bean
    public TaskletFileImportVasp taskletFileImportVasp() {
        return new TaskletFileImportVasp();
    }

    @Bean
    public TaskletFileImportVasp2 taskletFileImportVasp2() {
        return new TaskletFileImportVasp2();
    }

    @Bean
    public ExportFile exportFile() {
        return new ExportFile();
    }
}
