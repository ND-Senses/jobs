package vn.ndm.config;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ndm.listener.JobCompletionJobExecutionListener;
import vn.ndm.tasklet.bhtt.TaskletCreateFile;
import vn.ndm.tasklet.bhtt.TaskletImportBHTT;
import vn.ndm.tasklet.eoffice.ExportFile;
import vn.ndm.tasklet.ftp.DownloadFileFTP;
import vn.ndm.tasklet.ftp.UpLoadFileFTP;
import vn.ndm.tasklet.vasp.TaskletFileImportVasp;
import vn.ndm.tasklet.vasp.TaskletFileImportVasp2;
import vn.ndm.tasklet.vasp.TaskletImportOCSSP;

@Configuration
public class BeanConfiguration {
    @Bean("listener")
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionJobExecutionListener();
    }

    @Bean("tasklet-import-ocs")
    public TaskletImportOCSSP taskletImportOCSSP() {
        return new TaskletImportOCSSP();
    }

    @Bean("tasklet-create-file")
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

    @Bean("download-file-ftp")
    public DownloadFileFTP downloadFileFTP() {
        return new DownloadFileFTP();
    }

    @Bean("upload-file-ftp")
    public UpLoadFileFTP upLoadFileFTP() {
        return new UpLoadFileFTP();
    }
}
