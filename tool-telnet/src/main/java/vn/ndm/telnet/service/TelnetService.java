package vn.ndm.telnet.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
public class TelnetService {
    private final ThreadPoolTaskExecutor taskExecutor;
    String path = "telnet/servers.txt";

    @Autowired
    public TelnetService(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void init() {
        run();
    }

    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.path))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String[] ips = parts[1].split(" ");
                String[] ports = parts[2].split(" ");
                TelnetClient telnetClient = new TelnetClient();
                for (int i = 0; i < ips.length; i++) {
                    for (int j = 0; j < ports.length; j++) {
                        int finalJ = j;
                        int finalI = i;
                        taskExecutor.execute(() -> {
                            try{
                                telnetClient.connect(ips[finalI], Integer.parseInt(ports[finalJ]));
                                log.info("=====> {} Host {} port {} - Success", name, ips[finalI], ports[finalJ]);
                            } catch (IOException e) {
                                log.info("=====> {} Host {} port {} - Error {}", name, ips[finalI], ports[finalJ], e.getMessage());
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            log.info("Error reading file:     {}", e.getMessage());
        }
    }
}

