package org.levinson.photoetcher.server.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.levinson.photoetcher.server.exceptions.AuthorizationException;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    private final UserAuthTokenValidator tokenValidator;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = header.substring(BEARER.length());
        try {
            SecurityContextHolder.getContext().setAuthentication(tokenValidator.validateToken(token));
        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            throw new AuthorizationException("Invalid token", e);
        }

        filterChain.doFilter(request, response);
    }
}
