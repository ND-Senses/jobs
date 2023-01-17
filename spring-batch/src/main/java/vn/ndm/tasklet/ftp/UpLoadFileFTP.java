package vn.ndm.tasklet.ftp;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ndm.utils.FolderUtils;

@Component
public class UpLoadFileFTP implements Tasklet {
    @Autowired
    ConnectFTP connectFTP;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        upLoadFileFTP();
        return RepeatStatus.FINISHED;
    }

    public void upLoadFileFTP() {
        String pathLocal = FolderUtils.getFilePathToSave().getProperty("path-local");
        String pathServer = FolderUtils.getFilePathToSave().getProperty("path-server");

//        try {
//            ChannelSftp sftp = connectFTP.getConnect();
//            sftp.put(pathLocal, pathServer);
//            sftp.getExitStatus();
//            sftp.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
