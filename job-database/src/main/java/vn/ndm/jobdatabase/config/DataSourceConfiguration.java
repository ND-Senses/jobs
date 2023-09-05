package vn.ndm.jobdatabase.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class DataSourceConfiguration {

    public static final String ORACLE_JDBC_ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    public static final String COM_MYSQL_CJ_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_CLOUD = "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=yes)(ADDRESS=(PROTOCOL=TCP)(HOST=10.1.186.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.1.186.7)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=VSGDB)))";
    private static final String URL_VAS_OLD = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.54.9.53)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.54.9.53)(PORT = 1521))(LOAD_BALANCE = yes)(FAILOVER = yes))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME=vsgdb)(failover_mode=(type=session)(method=basic))))";

    @Bean
    public Map<String, DataSource> dataSourceMap() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("mysql", mysqlDataSource());
        dataSourceMap.put("9029", webCharging());
        dataSourceMap.put("eoffice", eofficeVNPT());
        dataSourceMap.put("eofficev2", eofficeVNPT2());
        return dataSourceMap;
    }

    @Bean("vgo")
    public List<DataSource> dataSourceMapVgo() {
        List<DataSource> dataSourceMap = new ArrayList<>();
        dataSourceMap.add(vasgateOld());
        dataSourceMap.add(vasgateOld1());
        dataSourceMap.add(vasgateOld2());
        dataSourceMap.add(vasgateOld3());
        dataSourceMap.add(vasgateOld4());
        dataSourceMap.add(vasgateOld5());
        dataSourceMap.add(vasgateOld6());
        return dataSourceMap;
    }

    @Bean("vgc")
    public List<DataSource> dataSourceMapCloud() {
        List<DataSource> dataSourceMap = new ArrayList<>();
        dataSourceMap.add(vasgateCloud());
        dataSourceMap.add(vasgateCloud1());
        dataSourceMap.add(vasgateCloud2());
        dataSourceMap.add(vasgateCloud3());
        dataSourceMap.add(vasgateCloud4());
        dataSourceMap.add(vasgateCloud5());
        dataSourceMap.add(vasgateCloud6());
        return dataSourceMap;
    }

    @Bean(value = "mysql")
    public DataSource mysqlDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(COM_MYSQL_CJ_JDBC_DRIVER);
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/admin");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    @Bean(value = "9029")
    public DataSource webCharging() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//10.252.10.221:1521/mpay");
        dataSourceBuilder.username("charging_admin");
        dataSourceBuilder.password("charging_admin");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vas-online")
    public DataSource vasOnline() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//10.252.10.221:1521/mpay");
        dataSourceBuilder.username("admin_msocial_sg");
        dataSourceBuilder.password("admin");
        return dataSourceBuilder.build();
    }

    @Bean(value = "eoffice")
    public DataSource eofficeVNPT() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//10.252.10.248:1521/eoffice1");
        dataSourceBuilder.username("cloud_admin");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    @Bean(value = "eofficev2")
    public DataSource eofficeVNPT2() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url("jdbc:oracle:thin:@//10.252.10.246:1521/eofdichvu");
        dataSourceBuilder.username("CLOUD_ADMIN_HOANGNM");
        dataSourceBuilder.password("vnpt123");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-admin_vg")
    public DataSource vasgateOld() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("admin_vg");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-cmdv_v3")
    public DataSource vasgateOld1() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("cmdv_v3");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-quartz")
    public DataSource vasgateOld2() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("quartz");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-csp_vg")
    public DataSource vasgateOld3() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("csp_vg");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-cmdv_v2_new")
    public DataSource vasgateOld4() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("cmdv_v2_new");
        dataSourceBuilder.password("cmdv");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-cmdv_sync")
    public DataSource vasgateOld5() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("cmdv_sync");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgo-cmdv_sms")
    public DataSource vasgateOld6() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_VAS_OLD);
        dataSourceBuilder.username("cmdv_sms");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-admin_vg")
    public DataSource vasgateCloud() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("admin_vg");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-cmdv_v3")
    public DataSource vasgateCloud1() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("cmdv_v3");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-quartz")
    public DataSource vasgateCloud2() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("quartz");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-csp_vg")
    public DataSource vasgateCloud3() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("csp_vg");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-cmdv_v2_new")
    public DataSource vasgateCloud4() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("cmdv_v2_new");
        dataSourceBuilder.password("cmdv");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-cmdv_sync")
    public DataSource vasgateCloud5() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("cmdv_sync");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }

    @Bean(value = "vgc-cmdv_sms")
    public DataSource vasgateCloud6() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(ORACLE_JDBC_ORACLE_DRIVER);
        dataSourceBuilder.url(URL_CLOUD);
        dataSourceBuilder.username("cmdv_sms");
        dataSourceBuilder.password("a");
        return dataSourceBuilder.build();
    }
}
