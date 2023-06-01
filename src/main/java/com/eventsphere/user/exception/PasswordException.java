package com.eventsphere.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown for password-related errors.
 *
 * <p>Example situations for this exception include:</p>
 * <ul>
 *   <li>Incorrect old password</li>
 *   <li>The confirmation password does not match the new password</li>
 * </ul>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordException extends RuntimeException {

    /**
     * Constructs a new instance of {@code PasswordException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public PasswordException(String message) {
        super(message);
    }
}