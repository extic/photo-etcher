package org.levinson.photoetcher.server.auth;

public record CredentialsDto(
        String username,
        String password
) {
}
