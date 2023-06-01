package com.eventsphere.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is not valid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotValidException extends RuntimeException {

    /**
     * Constructs a new instance of {@code UserNotValidException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserNotValidException(String message) {
        super(message);
    }
}
