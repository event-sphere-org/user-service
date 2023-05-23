package com.eventsphere.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested user cannot be found.
 *
 * <p>Example situation for this exception includes:</p>
 * <ul>
 *   <li>When attempting to retrieve a user by an ID that does not exist</li>
 * </ul>
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new instance of {@code UserNotFoundException} with the specified user ID.
     *
     * @param id the ID of the user that cannot be found.
     */
    public UserNotFoundException(Long id) {
        super("Can't find user with id " + id);
    }
}