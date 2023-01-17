package vn.ndm.tasklet.ftp;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DownloadFileFTP implements Tasklet {
    @Autowired
    ConnectFTP connectFTP;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        downloadFileFTP();
        return RepeatStatus.FINISHED;
    }

    public void downloadFileFTP() {
        String pathLocal = "D:/example/jobs/spring-batch/excel2/";
        String pathServer = "/app/data_test/dcm_track_20230116163802.xlsx";
        try {
            ChannelSftp sftp = connectFTP.getConnect();
            sftp.get(pathServer, pathLocal);
            sftp.getExitStatus();
            sftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
