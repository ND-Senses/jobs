package vn.ndm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author cimit
 * @Date: 28-05-2021
 */
public class FileUtils {
    public FileUtils() {
        // TODO document why this constructor is empty
    }

    public static String readFile(String file) {
        File f = new File(file);
        if (f.isFile()) {
            return readFile(f);
        }
        return null;
    }

    public static String readFile(File f) {
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile2(String f) {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
