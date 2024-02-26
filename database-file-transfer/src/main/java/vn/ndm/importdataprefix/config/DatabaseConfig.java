package vn.ndm.importdataprefix.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean("testbed")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://10.252.10.149:3306/smsc?useUnicode=yes&characterEncoding=UTF-8")
                .username("smsc")
                .password("smsc123")
                .build();
    }

    @Bean("local")
    public DataSource dataSourceLocal() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/smsc?useUnicode=yes&characterEncoding=UTF-8")
                .username("root")
                .password("123456")
                .build();
    }
}
