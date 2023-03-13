package vn.ndm.cleartrash.service;

import com.ibm.icu.text.Normalizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class FileSplitterService {

    public void searchFiles(String directory) {
        File folder = new File(directory);
        // Lấy danh sách các file trong thư mục
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // Nếu file là thư mục, gọi lại hàm searchFiles đệ quy vào thư mục đó
                if (file.isDirectory()) {
                    searchFiles(file.getPath());
                } else {
                    log.info("Parent: {}", file.getParent());
                    // Xử lý file tại đây
                    String filePath = file.getPath();
//                    delFile(file);
                    writeFile(filePath);
                }
            }
        }
    }

    public void writeFile(String path) {
        String outputFileWithDiacritics = "_vn.txt";
        String outputFileWithoutDiacritics = "_un_vn.txt";
        File outputWithDiacriticsFile = new File(path.replaceFirst("[.][^.]+$", "") + outputFileWithDiacritics);
        File outputWithoutDiacriticsFile = new File(path.replaceFirst("[.][^.]+$", "") + outputFileWithoutDiacritics);
        try (BufferedReader reader = new BufferedReader(new FileReader(path));
             PrintWriter writerWithDiacritics = new PrintWriter(outputWithDiacriticsFile);
             PrintWriter writerWithoutDiacritics = new PrintWriter(outputWithoutDiacriticsFile);) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Kiểm tra xem dòng có dấu hay không
                boolean hasDiacritics = false;
                for (char c : line.toCharArray()) {
                    if (hasDiacritics(c)) {
                        hasDiacritics = true;
                        break;
                    }
                }
                // Ghi dòng vào tệp tương ứng
                if (hasDiacritics) {
                    writerWithDiacritics.println(line);
                } else {
                    writerWithoutDiacritics.println(line);
                }
            }
            log.info("Split file: {} success!", new File(path).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasDiacritics(char s) {
        String normalized = Normalizer.normalize(s, Normalizer.NFC);
        return normalized.matches(".*[àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵ].*");
    }
}
