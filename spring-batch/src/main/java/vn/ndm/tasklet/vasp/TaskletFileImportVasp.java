package vn.ndm.tasklet.vasp;

import vn.ndm.obj.PackageVasp;
import vn.ndm.utils.FolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class TaskletFileImportVasp implements Tasklet {
    @Autowired
    @Qualifier("ocs-testbed")
    DataSource dataSource;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        writeFile();
        return RepeatStatus.FINISHED;
    }

    public static void checkExists(File p) {
        try {
            boolean isDirectoryCreated = p.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = p.mkdirs();
            }
            if (isDirectoryCreated) {
                log.info("File not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Map<String, PackageVasp> getExcelDataAsList(String path) {
        String pattern = "\t";
        Map<String, PackageVasp> stringList = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int t1 = 0;
            int t2 = 0;
            PackageVasp vasp = new PackageVasp();
            while ((line = br.readLine()) != null) {
                t1 = t2;
                t2 = line.lastIndexOf(pattern);

                if (line.contains("0==")) {
                    String[] strings = line.split("==");
                    vasp.setOcsCmd(strings[1].trim());
                }
                if (line.contains("1==")) {
                    String[] strings = line.split("==");
                    vasp.setServiceNumber(strings[1].trim());
                }
                if (line.contains("2==")) {
                    String[] strings = line.split("==");
                    vasp.setCommandCode(strings[1].trim());
                }
                if (line.contains("3==")) {
                    String[] strings = line.split("==");
                    vasp.setCommandArg(strings[1].trim());
                }
                if (line.contains("4==")) {
                    String[] strings = line.split("==");
                    vasp.setSourceCode(strings[1].trim());
                }
                if (line.contains("6==")) {
                    String[] strings = line.split("==");
                    vasp.setListSucc(strings[1].trim());
                }
                if (t2 == 1 && t1 == 2) {
                    stringList.put(vasp.getOcsCmd(), vasp);
                    vasp = new PackageVasp();
                }
            }
            return stringList;
        } catch (Exception e) {
            log.info("Error read file: {}", e.getMessage());
        }
        return new LinkedHashMap<>();
    }

    public static void writeFile() {
        String pathFile = FolderUtils.getFilePathToSave().getProperty("path");
        String pathSave = FolderUtils.getFilePathToSave().getProperty("path-save");
        checkExists(new File(pathSave));
        List<String> listForder = listFilesForFolder(new File(pathFile));
        LinkedHashMap<String, PackageVasp> total = new LinkedHashMap<>();
        for (String path : listForder) {
            Map<String, PackageVasp> packgeVasps = getExcelDataAsList(path);
            total.putAll(packgeVasps);

        }
        for (Map.Entry<String, PackageVasp> entry : total.entrySet()) {
            writeObjectToFile(total.get(entry.getKey()), pathSave);
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

    public static void writeObjectToFile(PackageVasp vasp, String filepath) {
        String filename = FolderUtils.getFilePathToSave().getProperty("filename");
        String path = filepath + filename;
        try (FileWriter fw = new FileWriter(path, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            StringBuilder builder = new StringBuilder();
            if (vasp.getOcsCmd() == null) vasp.setOcsCmd("");
            if (vasp.getServiceNumber() == null) vasp.setServiceNumber("");
            if (vasp.getCommandCode() == null) vasp.setCommandCode("");
            if (vasp.getCommandArg() == null) vasp.setCommandArg("");
            if (vasp.getSourceCode() == null) vasp.setSourceCode("");
            if (vasp.getListSucc() == null) vasp.setListSucc("");
            builder.append(vasp.getOcsCmd()).append(";").
                    append(vasp.getServiceNumber()).append(";").
                    append(vasp.getCommandCode()).append(";").
                    append(vasp.getCommandArg()).append(";").
                    append(vasp.getSourceCode()).append(";").
                    append(vasp.getListSucc().replace("|", ","));
            out.println(builder);
            System.out.println("The Object  was succesfully written to a file");
        } catch (Exception ex) {
            log.info("Error read file: {}", ex.getMessage());
        }
    }
}