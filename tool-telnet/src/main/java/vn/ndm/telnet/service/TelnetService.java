package vn.ndm.telnet.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
public class TelnetService {

    String path = "telnet/servers.txt";

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
                String host = parts[1];
                int port = Integer.parseInt(parts[2]);
                if (host.contains(" ")) {
                    for (String h : host.split(" "))
                        telnet(name, h, port);
                    continue;
                }
                telnet(name, host, port);
            }
        } catch (IOException e) {
            log.info("Error reading file: {}", e.getMessage());
        }
    }

    private void telnet(String name, String host, int port) {
        try{
            TelnetClient telnetClient = new TelnetClient();
            telnetClient.connect(host, port);
            log.info("=====> {} Host {} port {} - Success", name, host, port);
        } catch (IOException e) {
            log.info("=====> {} Host {} port {} - Error {}", name, host, port,e.getMessage());
        }
    }
}

