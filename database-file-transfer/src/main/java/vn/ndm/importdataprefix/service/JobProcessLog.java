package vn.ndm.importdataprefix.service;//package com.ndm.fakeapi;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zaxxer.hikari.HikariDataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class JobProcessLog extends Thread {
//    private final Logger logger = LoggerFactory.getLogger(JobProcessLog.class);
//    private final AtomicBoolean running = new AtomicBoolean(true);
//    private static final Logger logOfDay = LoggerFactory.getLogger("logs-of-day");
//    private boolean isConnected = false;
//    private String sql;
//    private String path = null;
//    private String pattern = null;
//    private String pathRetry = null;
//    private int batchSize = 5000;
//    private int maxRetry = 3;
//    private String pathFailed = null;
//    private long sleep = 10000L;
//    private static int count = 0;
//    private final Map<String, Integer> storeFileFail = new HashMap<>();
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public void setSql(String sql) {
//        this.sql = sql;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public void setPattern(String pattern) {
//        this.pattern = pattern;
//    }
//
//    public void setPathRetry(String pathRetry) {
//        this.pathRetry = pathRetry;
//    }
//
//    public void setBatchSize(int batchSize) {
//        this.batchSize = batchSize;
//    }
//
//    public void setMaxRetry(int maxRetry) {
//        this.maxRetry = maxRetry;
//    }
//
//    public void setPathFailed(String pathFailed) {
//        this.pathFailed = pathFailed;
//    }
//
//    public void setSleep(long sleep) {
//        this.sleep = sleep;
//    }
//
//    @Autowired
//    private HikariDataSource ds;
//    //connect
//    private PreparedStatement pstmt = null;
//    private Connection conn = null;
//
//    @Override
//    public void run() {
//        while (this.running.get()) {
//            try{
//                this.execute();
//                Thread.sleep(sleep);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                try{
//                    Thread.sleep(200);
//                } catch (InterruptedException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void execute() {
//        logger.debug("000000000000000000000000000000000000000000000");
//        this.readFileLog(this.path, false);
//        this.readFileLog(this.pathRetry, true);
//    }
//
//    public void readFileLog(String path, boolean isReload) {
//        File directory = new File(path);
//        File[] fileList = directory.listFiles();
//        if (fileList != null) {
//            this.logger.debug("found log loadlog path: {}, pattern: {}", path, this.pattern);
//            this.initConnect();
//            for (File f : fileList) {
//                if (f.isFile()) {
//                    this.readFile(f, isReload);
//                }
//            }
//            this.disconnect();
//        }
//    }
//
//    private void readFile(File f, boolean isReload) {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))){
//            long s = System.nanoTime();
//            String content;
//
//            while ((content = reader.readLine()) != null) {
//                if (!isReload) {
//                    logOfDay.info(content);
//                }
//
//                if (this.isConnected) {
//                    ++count;
//                    processLog(content, s, count);
//                }
//            }
//
//            handleFileAfterRead(f, isReload);
//            commitFinal(isConnected);
//        } catch (Exception e) {
//            logger.error("Exception: {}", e.getMessage());
//        }
//    }
//
//    private void processLog(String content, long startTime, int count) {
//        if (count > this.batchSize) {
//            this.commit(startTime);
//            count = 0;
//        }
//        insertLog(content);
//    }
//
//    private void handleFileAfterRead(File f, boolean isReload) throws IOException {
//        if (this.isConnected && this.commit(System.nanoTime())) {
//            this.logger.info(">>>>>>>>>>>>>>>> delete file: {} status: {}", f.getAbsolutePath(), f.delete());
//        }else{
//            handleFileRenameAndRetry(f, isReload);
//        }
//    }
//
//    private void handleFileRenameAndRetry(File f, boolean isReload) throws IOException {
//        if (this.maxRetry == -1) {
//            if (!isReload) {
//                renameFile(this.pathRetry, f);
//            }
//        }else if (isReload) {
//            handleReloadRetry(f);
//        }else{
//            storeFileFailAndRetry(f);
//        }
//    }
//
//    private void handleReloadRetry(File f) throws IOException {
//        Integer current = this.storeFileFail.get(f.getName());
//        if (current != null) {
//            if (current < this.maxRetry) {
//                current = current + 1;
//                this.storeFileFail.put(f.getName(), current);
//            }else{
//                renameFile(this.pathFailed, f);
//                this.storeFileFail.remove(f.getName());
//            }
//        }
//    }
//
//    private void storeFileFailAndRetry(File f) throws IOException {
//        this.storeFileFail.put(f.getName(), 0);
//        renameFile(this.pathRetry, f);
//    }
//
//    private void commitFinal(boolean isConnected) throws SQLException {
//        if (isConnected) {
//            this.conn.commit();
//        }
//    }
//
//    private void renameFile(String targetPath, File sourceFile) throws IOException {
//        checkExists(new File(targetPath));
//        Path target = Paths.get(targetPath + "/" + sourceFile.getName());
//        Files.move(sourceFile.toPath(), target);
//        this.logger.error(">>>>>>>>>>>>>>>> load fail, rename to: {}/{}", targetPath, sourceFile.getName());
//    }
//
//    public void insertLog(String content) {
//        System.out.println(content);
//    }
//
//    private boolean commit(long startTime) {
//        if (!this.isConnected) {
//            return false;
//        }else{
//            try{
//                long a = this.pstmt.executeBatch().length;
//                String time = estimateTime(startTime);
//                this.logger.info("Commit successful num: {} rows; execute time: {}", a, time);
//                this.pstmt.clearBatch();
//                this.pstmt.clearParameters();
//                return true;
//            } catch (Exception var6) {
//                try{
//                    this.conn.rollback();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                this.logger.error("Commit Exception: {}", var6.getMessage());
//                return false;
//            }
//        }
//    }
//
//    public static String estimateTime(long startTime) {
//        long value = System.nanoTime() - startTime;
//        if (value / Math.pow(10.0D, 9.0D) < 1.0D) {
//            if (value / Math.pow(10.0D, 6.0D) < 1.0D) {
//                return value / Math.pow(10.0D, 3.0D) < 1.0D ? value + " nanoseconds" : TimeUnit.MICROSECONDS.convert(value, TimeUnit.NANOSECONDS) + " microseconds";
//            }else{
//                return TimeUnit.MILLISECONDS.convert(value, TimeUnit.NANOSECONDS) + " milliseconds";
//            }
//        }else{
//            return TimeUnit.SECONDS.convert(value, TimeUnit.NANOSECONDS) + " seconds";
//        }
//    }
//
//    public void initConnect() {
//        try{
//            if (this.isConnected) {
//                return;
//            }
//            this.isConnected = true;
//            this.conn = this.ds.getConnection();
//            this.conn.setAutoCommit(false);
//            this.pstmt = this.conn.prepareStatement(this.sql);
//        } catch (Exception var2) {
//            this.logger.error("connect database log fail");
//            this.isConnected = false;
//            this.logger.error("initConnect Exception: {}", var2.getMessage());
//        }
//
//    }
//
//    public void disconnect() {
//        this.isConnected = false;
//        try{
//            this.pstmt.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try{
//            this.conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void checkExists(File p) {
//        try{
//            boolean isDirectoryCreated = p.exists();
//            if (!isDirectoryCreated) {
//                isDirectoryCreated = p.mkdirs();
//            }
//            if (isDirectoryCreated) {
//                logger.info("File not found");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void close() throws IOException {
//        this.logger.info("Shutdown job load gw");
//        this.running.set(false);
//    }
//}
//
