package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CleanService {

    public void getInfoFIle(String path) {
        List<String> strings = getAllPath(path);
        for (String pathFile : strings) {
            File file = new File(pathFile);
            log.info("File name {} size {}", file.getName(), file.length());
        }
    }

    public void deleteRecursive(File path) {
        log.info("Cleaning out folder: {}", path.toString());
        try {
            for (File file : Objects.requireNonNull(path.listFiles())) {
                if (file.isDirectory()) {
                    deleteRecursive(file);
                    Path p = Paths.get(file.getPath());
                    log.info("Delete folder {} status: {}", p, Files.deleteIfExists(p));
                } else {
                    Path p = Paths.get(file.getPath());
                    log.info("Delete file {} status: {}", p, Files.deleteIfExists(p));
                }
            }
        } catch (Exception e) {
            log.info("#Error delete file: {}", e.getMessage());
        }
    }

    //get list file folder
    public List<String> getAllPath(String path) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File f = new File(path);
            for (File file : Objects.requireNonNull(f.listFiles())) {
                if (file.isDirectory()) {
                    log.info("get folder: {}", file.getAbsolutePath());
                    list.add(file.getAbsolutePath());
                    getAllPath(file.getAbsolutePath());
                } else {
                    log.info("get name: {}", file.getName());
                    list.add(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void renameFileExtension(String source, String newExtension) {
        String target;
        File file1 = new File(source);
        File[] files = file1.listFiles();
        assert files != null;
        log.info("size data {}", files.length);
        for (File file : Objects.requireNonNull(files)) {
            String currentExtension = getFileExtension(file.getAbsolutePath());
            if (currentExtension.contains("jfif")) {
                log.info("file: {}", file.getAbsoluteFile());
                target = file.getAbsolutePath().replaceFirst(Pattern.quote("." + currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));
                boolean checkExits = new File(file.getAbsolutePath()).renameTo(new File(target));
                if (checkExits) {
                    log.info("Succsess: {}", target);
                }
            }
        }
    }

    public String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }


}
