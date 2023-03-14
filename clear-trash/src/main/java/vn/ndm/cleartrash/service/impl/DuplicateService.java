package vn.ndm.cleartrash.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.itf.JobHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class DuplicateService implements JobHandler {

    public void duplicateFile(String path) {
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (int x = 0; x < fileList.length; x++) {
                for (int y = x + 1; y < fileList.length; y++) {
                    if (fileList[x].length() == fileList[y].length() && compareFiles(fileList[x], fileList[y])) {
                        Path p = Paths.get(String.valueOf(fileList[x]));
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static boolean compareFiles(File x, File y) {
        try (FileInputStream xs = new FileInputStream(x);
             FileInputStream ys = new FileInputStream(y)) {
            log.info("Compare: " + x + " vs " + y);
            boolean result = true;
            while (result) {
                int xb = xs.read();
                int yb = ys.read();
                if (xs.read() != ys.read()) {
                    result = false;
                }
                if (xb == -1 || yb == -1)
                    result = false;
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void execute(List<String> value) {
        for (String v : value)
            duplicateFile(v);
    }
}
