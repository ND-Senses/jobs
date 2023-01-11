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
    @Bean(name = "mysql")
    public DataSource mysqlDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(COM_MYSQL_CJ_JDBC_DRIVER);
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/admin");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    @Bean(name = "ocs-testbed")
    public DataSource ocsLogDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@10.252.10.246:1521:ocs");
        dataSourceBuilder.username("ocs");
        dataSourceBuilder.password("ocssupport123");
        return dataSourceBuilder.build();
    }

    @Bean(name = "ocs-pro")
    public DataSource ocsProDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@10.252.10.246:1521:ocs");
        dataSourceBuilder.username("ocs");
        dataSourceBuilder.password("ocssupport123");
        return dataSourceBuilder.build();
    }

    @Bean(name = "bhtt")
    public DataSource bhttDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//localhost:1521/bhttdb");
        dataSourceBuilder.username("OCSSP");
        dataSourceBuilder.password("ocsSP$2021");
        return dataSourceBuilder.build();
    }

    @Bean(name = "e-office")
    public DataSource eOfficeDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@10.193.72.23:1521:eofdichvu");
        dataSourceBuilder.username("CLOUD_ADMIN_DEV_NEO_1");
        dataSourceBuilder.password("vnpt123");
        return dataSourceBuilder.build();
    }
}
