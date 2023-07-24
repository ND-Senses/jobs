package vn.ndm.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    public static final String ORACLE_JDBC_ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    public static final String COM_MYSQL_CJ_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    @Primary
    @Bean(name = "oracle")
    public DataSource mysqlDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//localhost:1521/orcl");
        dataSourceBuilder.username("manhnd");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }
}
