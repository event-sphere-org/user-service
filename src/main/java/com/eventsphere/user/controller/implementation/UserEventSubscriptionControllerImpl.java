package com.eventsphere.user.controller.implementation;

import com.eventsphere.user.controller.UserEventSubscriptionController;
import com.eventsphere.user.model.UserEventSubscription;
import com.eventsphere.user.model.dto.EventDto;
import com.eventsphere.user.service.UserEventSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Default implementation of {@link UserEventSubscriptionController}
 */
@RestController
@RequestMapping("v1/users/{userId}/subscriptions/events")
@RequiredArgsConstructor
public class UserEventSubscriptionControllerImpl implements UserEventSubscriptionController {

    private final UserEventSubscriptionService userEventSubscriptionService;

    @Override
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<EventDto>> getAllEventSubscriptions(
            @PathVariable final Long userId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.getAll(userId, page, size));
    }

    @Override
    @GetMapping(value = "/{eventId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserEventSubscription> getEventSubscription(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.get(userId, eventId));
    }

    @Override
    @PostMapping(value = "/{eventId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserEventSubscription> subscribeToEvent(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.subscribeUserToEvent(userId, eventId));
    }

    @Override
    @DeleteMapping(value = "/{eventId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> unsubscribeFromEvent(
            @PathVariable final Long userId,
            @PathVariable final Long eventId
    ) {
        userEventSubscriptionService.unsubscribeUserFromEvent(userId, eventId);
        return ResponseEntity.ok().build();
    }
}
