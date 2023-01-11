package vn.ndm.tasklet.bhtt;

import vn.ndm.itf.FileService;
import vn.ndm.utils.FolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@Slf4j
public class TaskletCreateFile implements FileService, Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        createFile();
        return RepeatStatus.FINISHED;
    }

    public void createFile() {
        log.info("Start create file");
        String folder = FolderUtils.getFilePathToSave().getProperty(("folder"));
        String nameFile = FolderUtils.getFilePathToSave().getProperty("name-file");
        int numberSerial = Integer.parseInt(FolderUtils.getFilePathToSave().getProperty("number-serial"));
        String serial = FolderUtils.getFilePathToSave().getProperty("serial");
        checkFolderisExists(new File(folder));
        String path = folder + nameFile;
        try (FileWriter fw = new FileWriter(path, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)){
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder().append(serial);
            String activeStatus = "0";
            String serialType = "1";
            for (int i = 0; i < (numberSerial / 100); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 100; j++) {
                    String s1 = String.valueOf(i * 100 + j);
                    sb.append(i).append(",").append(14750)
                            .append(",").append(37505)
                            .append(",").append(1)
                            .append(",")
                            .append(builder.replace(15 - s1.length(), 15, s1))
                            .append(",").append(random.nextInt(2))
                            .append(",").append(activeStatus)
                            .append(",").append("13-JULY-22 00:00:00").append(",")
                            .append(serialType)
                            .append("\n");
                }
                out.print(sb);
            }
            log.info("End create file");
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.info("Error created file: {}", e.getMessage());
        }
    }

    public static void checkFolderisExists(File p) {
        try{
            boolean isDirectoryCreated = p.exists();
            if (!isDirectoryCreated) {
                log.info("Folder does not exists!  --> Created");
                isDirectoryCreated = p.mkdirs();
                if (isDirectoryCreated) {
                    log.info("Folder create succ!");
                }
            }else{
                log.info("Folder is exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Folder create error!");

        }
    }

    public static List<String> listFilesForFolder(File folder) {
        List<String> list = new ArrayList<>();
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            list.add(fileEntry.getAbsolutePath());
        }
        return list;
    }
}