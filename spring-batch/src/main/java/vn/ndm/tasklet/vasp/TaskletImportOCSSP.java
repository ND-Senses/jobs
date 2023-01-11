package vn.ndm.tasklet.vasp;

import vn.ndm.utils.FolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ADMIN
 * Date: 5/9/2022 - Time: 11:46 AM
 * Project: sync-files
 **/
@Slf4j
public class TaskletImportOCSSP implements Tasklet {
    @Autowired
    @Qualifier("mysql")
    DataSource dataSource;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//        String folder = FolderUtils.getFilePathToSave().getProperty("path-save");
        String folder2 = FolderUtils.getFilePathToSave().getProperty("path-save2");
//        List<String> stringList = listFilesForFolder(new File(folder));
//        if (!stringList.isEmpty()) {
//            for (String path : stringList) {
//                List<String> re = getExcelDataAsList(path);
//                textImportDataBase(re);
//            }
//        }
//        List<String> list = listFilesForFolder(new File(folder2));
//        if (!list.isEmpty()) {
//            for (String path : list) {
//                List<String> re = getExcelDataAsList(path);
//                textImportDataBase2(re);
//            }
//        }

        return RepeatStatus.FINISHED;
    }


    public List<String> getExcelDataAsList(String path) {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringList.add(line);
            }
            return stringList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void textImportDataBase(List<String> line) {
        log.info("textImportDataBase: " + line.size());
        String sqlInsert = FolderUtils.getFilePathToSave().getProperty("sql-insert-package");
        String urlDb = FolderUtils.getFilePathToSave().getProperty("url-db");
        String userName = FolderUtils.getFilePathToSave().getProperty("user_name");
        String password = FolderUtils.getFilePathToSave().getProperty("password");
        try (Connection conn = DriverManager.getConnection(urlDb, userName, password);
             PreparedStatement ptsm = conn.prepareStatement(sqlInsert)) {
            if (!line.isEmpty()) {
                for (String s : line) {
                    String[] str = s.split(";");
                    for (int i = 0; i < str.length; i++) {
                        ptsm.setString(i + 1, str[i]);
                    }
                    ptsm.addBatch();
                }
                ptsm.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.info("Eó kết nối đc... {}", e.getMessage());
            e.getMessage();
        }
    }

    public void textImportDataBase2(List<String> line) {
        log.info("textImportDataBase2: " + line.size());
        String sqlInsert = FolderUtils.getFilePathToSave().getProperty("sql-insert-package2");
        String urlDb = FolderUtils.getFilePathToSave().getProperty("url-db");
        String userName = FolderUtils.getFilePathToSave().getProperty("user_name");
        String password = FolderUtils.getFilePathToSave().getProperty("password");

        try (Connection conn = DriverManager.getConnection(urlDb, userName, password);
             PreparedStatement ptsm = conn.prepareStatement(sqlInsert)) {
            if (!line.isEmpty()) {
                for (String s : line) {
                    String[] str = s.split(";");
                    for (int i = 0; i < str.length; i++) {
                        if (str[i] == null) ptsm.setString(i + 1, "");
                        ptsm.setString(i + 1, str[i]);
                    }
                    ptsm.addBatch();
                }
                ptsm.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.info("Eó kết nối đc... {}", e.getMessage());
            e.getMessage();
        }
    }

    public static List<String> listFilesForFolder(File folder) {
        List<String> list = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                list.add(fileEntry.getAbsolutePath());
            } else {
                list.add(fileEntry.getAbsolutePath());
            }
        }
        return list;
    }

}
