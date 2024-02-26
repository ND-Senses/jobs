package vn.ndm.importdataprefix.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Service
public class JobImport implements Runnable {

    private final DataSource dataSource;
    private String SQL = "insert into api_msisdn_app (msisdn, imsi, sys_id, active, edit_date, edit_user, insert_date, api_sync_id, sri, type) VALUES (?,?,?,?,sysdate(),?,sysdate(),?,?,?)";
    private final String path = "data/";

    @Autowired
    public JobImport(@Qualifier("testbed") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        log.info("JobImport started.");
        try {
            readFileLog();
        } catch (Exception e) {
            log.error("Error in JobImport: {}", e.getMessage());
        }
        log.info("JobImport finished.");
    }

    public void readFileLog() {
        try {
            File directory = new File(path);
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (f.isFile()) {
                        importDataFromFile(f);
                    }
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importDataFromFile(File file) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            log.info("Importing data from file: {}", file.getAbsolutePath());
            connection.setAutoCommit(false); // Enable transaction
            int batchSize = 0; // Đếm số lượng dòng trong batch

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(">> {}", line);
                    // Thêm dòng vào batch
                    String[] parts = line.split(",");
                    for (int i = 0; i < parts.length; i++) {
                        preparedStatement.setString(i + 1, parts[i]);
                    }
                    preparedStatement.addBatch();
                    // Tăng kích thước batch
                    batchSize++;
                    // Nếu batch đủ lớn, thực hiện executeBatch
                    if (batchSize >= 100000) {
                        executeBatch(preparedStatement, connection);
                        batchSize = 0; // Reset batchSize
                    }
                }

                // Thực hiện executeBatch cho các dòng cuối cùng nếu còn lại trong batch
                if (batchSize > 0) {
                    executeBatch(preparedStatement, connection);
                }
            }
        } catch (Exception e) {
            log.error("Error importing data:", e);
        }
    }

    private static void executeBatch(PreparedStatement preparedStatement, Connection connection) throws SQLException {
        try {
            log.info("Start executing batch");
            preparedStatement.executeBatch();
            connection.commit(); // Commit batch
        } catch (SQLException e) {
            log.error("Error executing batch:", e);
            connection.rollback(); // Rollback batch
            throw e; // Re-throw the exception
        } finally {
            preparedStatement.clearBatch(); // Clear the batch
        }
    }

}
