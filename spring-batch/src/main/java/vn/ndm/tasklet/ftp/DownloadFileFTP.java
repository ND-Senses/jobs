package vn.ndm.tasklet.ftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
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
    private final ConnectFTP connectFTP;
    private final List<String> listCache = new ArrayList<>();

    @Autowired
    public DownloadFileFTP(ConnectFTP connectFTP) {
        this.connectFTP = connectFTP;
    }

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
            // đồng bộ file nếu trên server đã có
            syncStore(sftp, pathServer);
            if (!listCache.isEmpty()) {
                for (String pathFile : listCache) {
                    sftp.get(pathFile, pathLocal);
                }
            }
            sftp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // đệ quy all file từ server
    public List<String> listFilesOnServer(ChannelSftp sftp, String path) throws SftpException {
        String delimiter = "/";
        List<String> fileList = new ArrayList<>();
        List<ChannelSftp.LsEntry> list = sftp.ls(path);
        for (ChannelSftp.LsEntry entry : list) {
            String fileName = entry.getFilename();
            if (entry.getAttrs().isDir()) {
                if (!fileName.equals(".") && !fileName.equals("..")) {
                    String subPath = path + delimiter + fileName;
                    fileList.addAll(listFilesOnServer(sftp, subPath));
                }
            } else {
                String fullPath = path + delimiter + fileName;
                fileList.add(fullPath);
            }
        }
        return fileList;
    }

    // sync file
    public void syncStore(ChannelSftp sftp, String path) throws SftpException {
        List<String> listRemove = new ArrayList<>();
        // add remove
        for (String list : listCache) {
            if (list != null) {
                listRemove.add(list);
            }
        }
        //         update new info
        List<String> listServer = listFilesOnServer(sftp, path);
        listCache.addAll(listServer);
        // remove
        for (String listFileRemove : listRemove) {
            listCache.remove(listFileRemove);
        }
    }
}
