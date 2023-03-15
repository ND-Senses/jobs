package vn.ndm.cleartrash.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ndm.cleartrash.itf.JobHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class CleanWithNameService implements JobHandler {

    public static void delWithCondition(String directory, String regex) {
        try {
            File folder = new File(directory);
            // Lấy danh sách các file trong thư mục
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Nếu file là thư mục, gọi lại hàm delWithCondition đệ quy vào thư mục đó
                    if (file.isDirectory()) {
                        delWithCondition(file.getPath(), regex);
                    } else {
                        if (file.getName().matches(regex)) {
                            Path p = Paths.get(file.getPath());
                            log.info("Delete file {} status: {}", p, Files.deleteIfExists(p));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(List<String> value) {
        for (String v : value) {
            String[] arr = v.split(",");
            delWithCondition(arr[0], arr[1]);
        }
    }
}
