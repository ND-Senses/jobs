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
import java.util.List;

@Slf4j
@Component
public class DownloadFileFTP implements Tasklet {
    @Autowired
    ConnectFTP connectFTP;
    private final List<String> listCache = new ArrayList<>();
    private final String delimeter = "/";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        log.info("#List store file: {}", listCache.size());
        downloadFileFTP();
        return RepeatStatus.FINISHED;
    }

    public void downloadFileFTP() {
        String pathLocal = FolderUtils.getFilePathToSave().getProperty("path-local");
        String pathServer = FolderUtils.getFilePathToSave().getProperty("path-server");
        try {
            FolderUtils.checkExists(new File(pathLocal));
            ChannelSftp sftp = connectFTP.getConnect();
            sftp.cd(pathServer);
            List<ChannelSftp.LsEntry> listUpdate = sftp.ls(pathServer);
            syncStore(listUpdate);
            if (listCache.isEmpty()) {
                for (ChannelSftp.LsEntry file : listUpdate) {
                    if (!file.getFilename().equals(".") && !file.getFilename().equals("..")) {
                        String pathFullServer = pathServer + delimeter + file.getFilename();
                        sftp.get(pathFullServer, pathLocal);
                    }
                }
            } else {
                for (String file : listCache) {
                    String pathFullServer = pathServer + delimeter + file;
                    sftp.get(pathFullServer, pathLocal);
                }
            }
            sftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncStore(List<ChannelSftp.LsEntry> listUpdate) {
        List<String> listRemove = new ArrayList<>();
        // add remove
        for (String list : listCache) {
            if (list != null) {
                listRemove.add(list);
            }
        }
        //         update new info
        for (ChannelSftp.LsEntry listEntry : listUpdate) {
            if (!listEntry.getFilename().equals(".") && !listEntry.getFilename().equals("..")) {
                listCache.add(listEntry.getFilename());
            }
        }
        // remove
        for (String listFileRemove : listRemove) {
            listCache.remove(listFileRemove);
        }
    }
}
