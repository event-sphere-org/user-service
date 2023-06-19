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
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructs a new instance of {@code EventNotFoundException} with the specified event ID.
     *
     * @param id the ID of the user that cannot be found.
     */
    public CategoryNotFoundException(Long id) {
        super("Can't find category with id " + id);
    }
}