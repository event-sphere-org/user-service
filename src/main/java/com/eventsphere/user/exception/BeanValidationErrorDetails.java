package com.eventsphere.user.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class BeanValidationErrorDetails {

    private final LocalDateTime timestamp;
    private final Map<String, List<String>> message;
    private final String details;

    public BeanValidationErrorDetails(LocalDateTime timestamp, Map<String, List<String>> message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
