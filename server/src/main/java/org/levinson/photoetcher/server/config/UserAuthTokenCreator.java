package org.levinson.photoetcher.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.levinson.photoetcher.server.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class UserAuthTokenCreator {

    private final String secretKey;

    public UserAuthTokenCreator(@Value("${security.jwt.key:secret-value}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User user) {
        var now = new Date();
        var validity = new Date(now.getTime() + 3_600_000L);

        var roles = user.roles().stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.joining(","));

        return JWT.create()
                .withSubject(user.username())
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }
}
