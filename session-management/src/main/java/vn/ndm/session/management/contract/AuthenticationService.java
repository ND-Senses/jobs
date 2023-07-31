package vn.ndm.session.management.contract;

import org.springframework.stereotype.Service;
import vn.ndm.session.management.obj.AuthenticatedUser;

@Service
public abstract class AuthenticationService {
    public abstract AuthenticatedUser login(String username, String password);

    public abstract void logout(String session);

    public abstract AuthenticatedUser keepAlive(String session);
}
