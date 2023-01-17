package vn.ndm.tasklet.ftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ConnectFTP {
    public ChannelSftp getConnect() {
        ChannelSftp sftp = null;
        try {
            String hostname = "10.252.10.248";
            String login = "root";
            String password = "123456a@";
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch ssh = new JSch();
            Session session = ssh.getSession(login, hostname, 22);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }
}
