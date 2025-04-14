package org.levinson.photoetcher.server.exceptions;

import lombok.Getter;

@Getter
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Exception cause) {
        super(message, cause);
    }
}
