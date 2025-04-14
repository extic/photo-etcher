package org.levinson.photoetcher.server.auth;

import lombok.RequiredArgsConstructor;
import org.levinson.photoetcher.server.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    @Value("${admin_password}")
    private String adminPassword;

    public User login(CredentialsDto credentials) {
        if (passwordEncoder.matches(CharBuffer.wrap(credentials.password()), adminPassword)) {
            return createUser();
        }

        throw new AuthorizationException("Invalid username or password for '" + credentials.username() + "'");
    }

    public User findByUsername(String username) {
        if (username.equals("admin")) {
            return createUser();
        }
        throw new AuthorizationException("Unknown user '" + username + "'");
    }

    private User createUser() {
        return new User(1L, "Admin", "Admin", "admin", adminPassword, Set.of(new Role(1L, "ADMIN")));
    }
}
