package vn.ndm.importdataprefix.service;//package com.ndm.fakeapi;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.sql.DataSource;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//@Slf4j
//@Service
//public class JobImportv2 implements Runnable {
//
//    private final DataSource dataSource;
//    private Connection connection;
//    private PreparedStatement preparedStatement;
//    private String SQL = "insert into api_msisdn_app (id, msisdn, imsi, sys_id, active, edit_date, edit_user, insert_date, api_sync_id, sri, type) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//    private final String path = "data/msisdn.txt";
//    private final int batchSize = 500000;
//
//    @Autowired
//    public JobImportv2(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public void run() {
//        log.info("JobImport started.");
//        try{
//            initConnect();
//            importDataFromFile();
//        } catch (Exception e) {
//            log.error("Error in JobImport: {}", e.getMessage());
//        } finally {
//            closeResources();
//        }
//        log.info("JobImport finished.");
//    }
//
//    private void initConnect() throws SQLException {
//        log.info("Initializing database connection.");
//        connection = dataSource.getConnection();
//        connection.setAutoCommit(false); // Enable transaction
//        preparedStatement = connection.prepareStatement(SQL);
//    }
//
//    private void importDataFromFile() throws IOException, SQLException {
//        log.info("Importing data from file: {}", path);
//        File file = new File(path);
//        if (!file.exists()) {
//            log.error("File not found: {}", path);
//            return;
//        }
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
//            String line;
//            int count = 0;
//            while ((line = reader.readLine()) != null) {
//                processData(line);
//                count++;
//                if (count % batchSize == 0) {
//                    executeBatch();
//                    count = 0; // Reset count after executing batch
//                }
//            }
//            // Execute the final batch (if any)
//            if (count > 0) {
//                executeBatch();
//            }
//        }
//    }
//
//
//    private void processData(String data) throws SQLException {
//        // Assuming data is comma-separated
//        log.info(">> Data {}", data);
//        String[] parts = data.split(",");
//        for (int i = 0; i < parts.length; i++) {
//            preparedStatement.setString(i + 1, parts[i]);
//        }
//        preparedStatement.addBatch();
//    }
//
//    private void executeBatch() throws SQLException {
//        log.info("Executing batch insert.");
//        preparedStatement.executeBatch();
//        connection.commit(); // Commit transaction
//        preparedStatement.clearBatch();
//    }
//
//    private void closeResources() {
//        log.info("Closing database connection.");
//        try{
//            if (preparedStatement != null) {
//                preparedStatement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (SQLException e) {
//            log.error("Error while closing resources: {}", e.getMessage());
//        }
//    }
//}
