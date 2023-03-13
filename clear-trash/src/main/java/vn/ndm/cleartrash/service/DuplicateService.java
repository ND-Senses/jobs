package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class DuplicateService {
    /**
     * check duplicate:
     * enCode
     * used FileInputStream read()
     *
     * @param path
     * @throws IOException
     */
    public void duplicateFile(String path) throws IOException {
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (int x = 0; x < fileList.length; x++) {
                for (int y = x + 1; y < fileList.length; y++) {
                    if (fileList[x].length() == fileList[y].length() && compareFiles(fileList[x], fileList[y])) {
                        Path p = Paths.get(String.valueOf(fileList[x]));
                        Files.deleteIfExists(p);
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

    public static void findDuplicateFiles(Map<String, List<String>> filesList, File directory) throws
            NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-512");
        File[] files = directory.listFiles();
        for (File dirChild : Objects.requireNonNull(files)) {
            // Iterate all file sub directories recursively
            if (dirChild.isDirectory()) {
                findDuplicateFiles(filesList, dirChild);
            } else {
                try (InputStream is = new FileInputStream(dirChild)) {
                    // Read file as bytes
                    byte[] buffer = new byte[1000];
                    while (is.read(buffer) > 0) {
                        String uniqueFileHash = new BigInteger(1, messageDigest.digest(buffer)).toString(16);
                        List<String> identicalList = filesList.get(uniqueFileHash);
                        if (identicalList == null) {
                            identicalList = new LinkedList<>();
                        }
                        // Add path to list
                        identicalList.add(dirChild.getAbsolutePath());
                        // push updated list to Hash table
                        filesList.put(uniqueFileHash, identicalList);
                    }
                } catch (IOException e) {
                    log.info("cannot read file " + dirChild.getAbsolutePath(), e);
                }
            }
        }
    }
}
