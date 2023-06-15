package com.eventsphere.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create a user that already exists.
 *
 * <p>Example situations for this exception include:</p>
 * <ul>
 *   <li>When attempting to create a subscription with a userId and eventId that is already registered in the system</li>
 * </ul>
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class SubscriptionAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new instance of {@code SubscriptionAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}