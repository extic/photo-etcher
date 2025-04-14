package org.levinson.photoetcher.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class UserAuthTokenValidator {

    private final String secretKey;

    public UserAuthTokenValidator(@Value("${security.jwt.key:secret-value}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Authentication validateToken(String token) {
        var verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();

        var decoded = verifier.verify(token);
        var username = decoded.getSubject();

        var authorities = Arrays.stream(decoded.getClaim("roles").asString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
