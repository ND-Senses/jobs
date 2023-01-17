package vn.ndm.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Command {
    @Autowired
    @Qualifier("mysql")
    DataSource dataSource;
    ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    public void getCommand() {

    }
}
