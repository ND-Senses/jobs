package vn.ndm.cleartrash.cache;

import org.springframework.stereotype.Component;
import vn.ndm.cleartrash.service.FileUltil;

import java.util.List;
import java.util.Map;

@Component
public class CmdCache {
    public Map<String, List<String>> load() {
        return FileUltil.readYamlFile("config/job-config.yml");
    }
}
