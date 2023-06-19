package com.eventsphere.user.controller;

import com.eventsphere.user.exception.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller interface for managing user subscriptions related operations.
 */
public interface SubscriptionController<T, S> {

    /**
     * Gets all subscriptions for user.
     *
     * @param userId ID of user to get item subs
     * @param page   page number
     * @param size   page size
     * @return ResponseEntity with the list of subscriptions and HTTP status OK.
     */
    @Operation(summary = "Retrieves all subscriptions for user", description = "Retrieves all subscriptions for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<List<S>> getAllSubscriptions(
            @Parameter(description = "ID of user to get subscriptions", required = true) final Long userId,
            final int page,
            final int size
    );

    /**
     * Gets specific subscription for user.
     *
     * @param userId ID of user to get event subscription
     * @param itemId ID of item
     * @return ResponseEntity with the subscription and HTTP status OK.
     */
    @Operation(summary = "Retrieves specific subscription for user", description = "Retrieves specific subscription for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User, subscription or item not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<T> getSubscription(
            @Parameter(description = "ID of user to get subscription", required = true) final Long userId,
            @Parameter(description = "ID of item", required = true) final Long itemId
    );

    /**
     * Subscribes user.
     *
     * @param userId ID of user
     * @param itemId ID of item
     * @return ResponseEntity with the created subscription and HTTP status CREATED.
     */
    @Operation(summary = "Subscribes user", description = "Creates new subscription for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Subscription already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User or item not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error saving to database",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<T> subscribe(
            @Parameter(description = "ID of user", required = true) final Long userId,
            @Parameter(description = "ID of item to subscribe", required = true) final Long itemId
    );

    /**
     * Unsubscribes user.
     *
     * @param userId ID of user
     * @param itemId ID of item
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Unsubscribes user", description = "Deletes subscription for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or subscription not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<Void> unsubscribe(
            @Parameter(description = "ID of user", required = true) final Long userId,
            @Parameter(description = "ID of item to unsubscribe", required = true) final Long itemId
    );
}
