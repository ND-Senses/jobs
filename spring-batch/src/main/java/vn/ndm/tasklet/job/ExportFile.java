package vn.ndm.tasklet.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vn.ndm.utils.FolderUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ExportFile implements Tasklet {

    private final DataSource dataSource;

    public ExportFile(@Qualifier("mysql") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        log.info("---------------------------------------------");
        queryData();
        return RepeatStatus.FINISHED;
    }

    public void queryData() {
        log.info("#exportExcelData");
        String nameTable = FolderUtils.getFilePathToSave().getProperty("name-table");
        String sqlExport = FolderUtils.getFilePathToSave().getProperty("sql-export");
        String fileFormat = FolderUtils.getFilePathToSave().getProperty("file-format");
        String[] arrTable = nameTable.split(",");
        for (String table : arrTable) {
            String timeSring = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            StringBuilder builder = new StringBuilder();
            String file;
            builder.append(table).append("_").append(timeSring).append(fileFormat);
            String folder = FolderUtils.getFilePathToSave().getProperty("folder-exp");
            FolderUtils.checkExists(new File(folder));
            try (Connection conn = dataSource.getConnection();
                 SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
                 PreparedStatement ptsm = conn.prepareStatement(sqlExport + table)) {
                FileOutputStream fileOut = null;
                Sheet sheet = workbook.createSheet(table);
                ResultSet rs = ptsm.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                Row desRow1 = sheet.createRow(0);
                for (int col = 0; col < columnsNumber; col++) {
                    Cell newpath = desRow1.createCell(col);
                    newpath.setCellValue(rsmd.getColumnLabel(col + 1));
                }
                log.info("Loading..............");
                while (rs.next()) {
                    Row desRow = sheet.createRow(rs.getRow());
                    for (int col = 0; col < columnsNumber; col++) {
                        Cell newpath = desRow.createCell(col);
                        newpath.setCellValue(rs.getString(col + 1));
                    }
                }
                file = builder.toString();
                fileOut = new FileOutputStream(folder + file);
                workbook.write(fileOut);
                workbook.dispose();
                fileOut.close();
                log.info("Export files success: {} table: {}", file, table);
            } catch (SQLException | IOException e) {
                log.info("Eó kết nối đc... {}", e.getMessage());
            }
        }
    }
}
