package vn.ndm.cleartrash.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class WaittingReadFile {
    private static final Map<WatchKey, Path> keyPathMap = new HashMap<>();
    public static void main(String[] args) throws Exception {
        log.info("Start");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            registerDir(Paths.get("job/061020220900/"), watchService);
            startListening(watchService);
        }
    }
    private static void registerDir(Path path, WatchService watchService) throws IOException {
        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }
        log.info("registering: " + path);
        WatchKey key = path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        keyPathMap.put(key, path);

        for (File f : Objects.requireNonNull(path.toFile().listFiles())) {
            registerDir(f.toPath(), watchService);
        }
    }

    private static void startListening(WatchService watchService) throws Exception {
        do {
            WatchKey queuedKey = watchService.take();
            for (WatchEvent<?> watchEvent : queuedKey.pollEvents()) {
                System.out.printf("Event... kind=%s, count=%d, context=%s Context type=%s%n",
                        watchEvent.kind(),
                        watchEvent.count(), watchEvent.context(),
                        ((Path) watchEvent.context()).getClass());
                //do something useful here

                if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    //this is not a complete path
                    Path path = (Path) watchEvent.context();
                    //need to get parent path
                    Path parentPath = keyPathMap.get(queuedKey);
                    //get complete path
                    path = parentPath.resolve(path);
                    log.info("===>" + readFile(path.toFile()));
                    registerDir(path, watchService);
                }
            }
            if (!queuedKey.reset()) {
                keyPathMap.remove(queuedKey);
            }
        } while (!keyPathMap.isEmpty());
    }

    public static String readFile(File f) {
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            return new String(bytes);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
