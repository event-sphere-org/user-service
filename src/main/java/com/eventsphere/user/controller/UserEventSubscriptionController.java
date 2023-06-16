package com.eventsphere.user.controller;

import com.eventsphere.user.exception.ErrorDetails;
import com.eventsphere.user.model.UserEventSubscription;
import com.eventsphere.user.model.dto.EventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller interface for managing user event subscription related operations.
 */
public interface UserEventSubscriptionController {

    /**
     * Gets all event subscriptions for user.
     *
     * @param userId ID of user to get event subs
     * @param page   page number
     * @param size   page size
     * @return ResponseEntity with the list of event subscriptions and HTTP status OK.
     */
    @Operation(summary = "Retrieves all event subscriptions for user", description = "Retrieves all event subscriptions for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = EventDto.class, type = "array")
            )),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<List<EventDto>> getAllEventSubscriptions(
            @Parameter(description = "ID of user to get event subs", required = true) final Long userId,
            final int page,
            final int size
    );

    /**
     * Gets specific event subscription for user.
     *
     * @param userId  ID of user to get event subscription
     * @param eventId ID of event
     * @return ResponseEntity with the event subscription and HTTP status OK.
     */
    @Operation(summary = "Retrieves specific event subscription for user", description = "Retrieves specific event subscription for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = UserEventSubscription.class)
            )),
            @ApiResponse(responseCode = "404", description = "User, event or subscription not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<UserEventSubscription> getEventSubscription(
            @Parameter(description = "ID of user to get event subscription", required = true) final Long userId,
            @Parameter(description = "ID of event", required = true) final Long eventId
    );

    /**
     * Subscribes user to event.
     *
     * @param userId  ID of user
     * @param eventId ID of event
     * @return ResponseEntity with the created subscription and HTTP status CREATED.
     */
    @Operation(summary = "Subscribes user to event", description = "Creates new subscription for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserEventSubscription.class))),
            @ApiResponse(responseCode = "409", description = "Subscription already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User or event not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error saving to database",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<UserEventSubscription> subscribeToEvent(
            @Parameter(description = "ID of user", required = true) final Long userId,
            @Parameter(description = "ID of event to subscribe", required = true) final Long eventId
    );

    /**
     * Unsubscribes user from event.
     *
     * @param userId  ID of user
     * @param eventId ID of event
     * @return ResponseEntity with HTTP status OK.
     */
    @Operation(summary = "Unsubscribes user from event", description = "Deletes subscription on event for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or subscription not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<Void> unsubscribeFromEvent(
            @Parameter(description = "ID of user", required = true) final Long userId,
            @Parameter(description = "ID of event to unsubscribe", required = true) final Long eventId
    );
}
