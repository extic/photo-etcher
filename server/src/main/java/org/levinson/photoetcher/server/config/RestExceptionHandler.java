package org.levinson.photoetcher.server.config;

import org.levinson.photoetcher.server.exceptions.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleAuthorizationException(AuthorizationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(ex.getMessage()));
    }

    public record ErrorDto(String message) {}
}
