package vn.ndm.session.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.ndm.session.management.contract.AuthenticationService;
import vn.ndm.session.management.obj.AuthenticatedUser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


@Configuration
public class BeanConfiguration {
    @Bean("BlockingQueue")
    public BlockingQueue<AuthenticatedUser> queue(@Value("${session-management.max-session}") int queueSize) {
        return new ArrayBlockingQueue<>(queueSize);
    }

    @Bean
    public AuthenticatedUser authenticatedUser() {
        return new AuthenticatedUser();
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService() {
            @Override
            public AuthenticatedUser login(String username, String password) {
                return null;
            }

            @Override
            public void logout(String session) {

            }

            @Override
            public AuthenticatedUser keepAlive(String session) {
                return null;
            }
        };
    }
}

