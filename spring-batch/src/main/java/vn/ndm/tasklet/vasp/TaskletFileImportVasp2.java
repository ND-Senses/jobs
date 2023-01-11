package vn.ndm.tasklet.vasp;

import vn.ndm.itf.FileService;
import vn.ndm.obj.PackageVasp2;
import vn.ndm.utils.FolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class TaskletFileImportVasp2 implements FileService, Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        writeFile();
        return RepeatStatus.FINISHED;
    }

    public void createFile() {
    }

    public static void checkExists(File p) {
        try{
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


    public static Map<String, PackageVasp2> getExcelDataAsList(String path) {
        String pattern = "\t";
        LinkedHashMap<String, PackageVasp2> stringList = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String line;
            int t1 = 0;
            int t2 = 0;
            PackageVasp2 vasp = new PackageVasp2();
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
                if (t2 == 1 && t1 == 2) {
                    stringList.put(vasp.getOcsCmd(), vasp);
                    vasp = new PackageVasp2();
                }
            }
            return stringList;
        } catch (Exception e) {
            log.info("Error read file: {}", e.getMessage());
        }
        return new LinkedHashMap<>();
    }

    public static void writeFile() {
        String pathFile = FolderUtils.getFilePathToSave().getProperty("path2");
        String pathSave = FolderUtils.getFilePathToSave().getProperty("path-save2");
        checkExists(new File(pathSave));
        List<String> listForder = listFilesForFolder(new File(pathFile));
        LinkedHashMap<String, PackageVasp2> total = new LinkedHashMap<>();
        for (String path : listForder) {
            Map<String, PackageVasp2> packgeVasps = getExcelDataAsList(path);
            total.putAll(packgeVasps);
        }
        for (String key : total.keySet()) {
            WriteObjectToFile(total.get(key), pathSave);
        }

    }

    public static List<String> listFilesForFolder(File folder) {
        List<String> list = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                list.add(fileEntry.getAbsolutePath());
            }else{
                list.add(fileEntry.getAbsolutePath());
            }
        }
        return list;
    }

    public static void WriteObjectToFile(PackageVasp2 vasp, String filepath) {
        String filename = FolderUtils.getFilePathToSave().getProperty("filename");
        String path = filepath + filename;
        try (FileWriter fw = new FileWriter(path, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)){
            StringBuilder builder = new StringBuilder();
            if (vasp.getOcsCmd() == null) vasp.setOcsCmd("");
            if (vasp.getServiceNumber() == null) vasp.setServiceNumber("");
            if (vasp.getCommandCode() == null) vasp.setCommandCode("");
            if (vasp.getCommandArg() == null) vasp.setCommandArg("");
            builder.append(vasp.getOcsCmd()).append(";").
                    append(vasp.getServiceNumber()).append(";").
                    append(vasp.getCommandCode()).append(";").
                    append(vasp.getCommandArg()).append(";").append(vasp.getListSucc());
            out.println(builder);
            System.out.println("The Object  was succesfully written to a file");
        } catch (Exception ex) {
            log.info("Error read file: {}", ex.getMessage());
        }
    }
}