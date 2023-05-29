package com.eventsphere.user.exception;

import com.eventsphere.user.util.ErrorUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global exception handler for the UserService.
 */
@ControllerAdvice
@Slf4j
public class UserServiceResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all exceptions and returns an error response with an internal server error status.
     *
     * @param ex      the exception to handle.
     * @param request the current request.
     * @return a ResponseEntity containing the error details and status.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));

        log.error(ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles UserNotFoundException and returns an error response with a not found status.
     *
     * @param ex      the exception to handle.
     * @param request the current request.
     * @return a ResponseEntity containing the error details and status.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));

        log.warn(ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserNotValidException and PasswordException and returns an error response with a bad request status.
     *
     * @param ex      the exception to handle.
     * @param request the current request.
     * @return a ResponseEntity containing the error details and status.
     */
    @ExceptionHandler({UserNotValidException.class, PasswordException.class})
    public final ResponseEntity<ErrorDetails> handleUserNotValidExceptionException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));

        log.warn(ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserAlreadyExistsException and returns an error response with a conflict status.
     *
     * @param ex      the exception to handle.
     * @param request the current request.
     * @return a ResponseEntity containing the error details and status.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));

        log.warn(ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Handles MethodArgumentNotValidException and returns an error response with a bad request status.
     *
     * @param ex      the exception to handle.
     * @param headers the headers for the response.
     * @param status  the status for the response.
     * @param request the current request.
     * @return a ResponseEntity containing the error details and status.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            WebRequest request
    ) {
        BeanValidationErrorDetails errorDetails = new BeanValidationErrorDetails(LocalDateTime.now(),
                ErrorUtils.getFieldErrors(ex.getFieldErrors()), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
