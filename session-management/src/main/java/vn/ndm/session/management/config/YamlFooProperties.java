package vn.ndm.session.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "session-management")
@PropertySource(value = "file:${user.dir}/session-management/config/application.yml", factory = YamlPropertySourceFactory.class)
public class YamlFooProperties {
    private int maxSession;
    private int timeKeepAlive;
    private int timeWaitFree;
    private int numberInitSession;
    private int minSession;
    private long sessionTimeOut;
    private long sessionAlive;
    private long fixeDelay;
}
