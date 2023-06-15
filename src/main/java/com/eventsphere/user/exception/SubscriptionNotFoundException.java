package com.eventsphere.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested user cannot be found.
 *
 * <p>Example situation for this exception includes:</p>
 * <ul>
 *   <li>When attempting to retrieve an event by an ID that does not exist</li>
 * </ul>
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}