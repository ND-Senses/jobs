package vn.ndm.cleartrash.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.itf.JobHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CleanService implements JobHandler {

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

    @Override
    public void execute(List<String> value) {
        for (String v : value) {
            deleteRecursive(new File(v));
        }
    }
}
