package vn.ndm.cleartrash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ndm.cleartrash.cache.CmdCache;

import java.util.List;
import java.util.Map;

@Configuration
public class ConfigApp {

    @Bean("cache")
    public Map<String, List<String>> cmdCache() {
        return new CmdCache().load();
    }
}
