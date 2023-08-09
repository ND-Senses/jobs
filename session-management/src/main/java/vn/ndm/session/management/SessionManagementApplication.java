package vn.ndm.session.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import vn.ndm.session.management.config.YamlFooProperties;

@SpringBootApplication
@EnableConfigurationProperties(YamlFooProperties.class)
public class SessionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionManagementApplication.class, args);
    }

}
