package org.levinson.photoetcher.server.auth;

import java.util.Set;

public record User(
    Long id,
    String firstName,
    String lastName,
    String username,
    String password,
    Set<Role> roles
) {}
