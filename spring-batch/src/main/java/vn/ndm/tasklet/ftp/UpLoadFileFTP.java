package vn.ndm.tasklet.ftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ndm.utils.FolderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UpLoadFileFTP implements Tasklet {
    private final ConnectFTP connectFTP;

    @Autowired
    public UpLoadFileFTP(ConnectFTP connectFTP) {
        this.connectFTP = connectFTP;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        upLoadFileFTP();
        return RepeatStatus.FINISHED;
    }

    public void upLoadFileFTP() {
        String pathLocal = FolderUtils.getFilePathToSave().getProperty("path-upload-local");
        String pathServer = FolderUtils.getFilePathToSave().getProperty("path-upload-server");
        try {
            List<File> files = deleteRecursive(new File(pathLocal));
            for (File file : files) {
                ChannelSftp sftp = connectFTP.getConnect();
                sftp.put(file.getAbsolutePath(), pathServer);
                sftp.getExitStatus();
                sftp.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<File> deleteRecursive(File path) {
        List<File> fileList = new ArrayList<>();
        log.info("deleteRecursive: {}", path.toString());
        try {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteRecursive(file);
                    } else {
                        fileList.add(file);
                    }
                }
            }
            return fileList;
        } catch (Exception e) {
            log.info("#Error deleteRecursive file: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
