package com.eventsphere.user.controller.implementation;

import com.eventsphere.user.controller.SubscriptionController;
import com.eventsphere.user.model.UserEventSubscription;
import com.eventsphere.user.model.dto.EventDto;
import com.eventsphere.user.service.implementation.UserEventSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Default implementation of {@link SubscriptionController}
 */
@RestController
@RequestMapping("v1/users/{userId}/subscriptions/events")
@RequiredArgsConstructor
public class UserEventSubscriptionController implements SubscriptionController<UserEventSubscription, EventDto> {

    private final UserEventSubscriptionService userEventSubscriptionService;

    @Override
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<EventDto>> getAllSubscriptions(
            @PathVariable final Long userId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.getAll(userId, page, size));
    }

    @Override
    @GetMapping(value = "/{itemId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserEventSubscription> getSubscription(
            @PathVariable final Long userId,
            @PathVariable final Long itemId
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.get(userId, itemId));
    }

    @Override
    @PostMapping(value = "/{itemId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserEventSubscription> subscribe(
            @PathVariable final Long userId,
            @PathVariable final Long itemId
    ) {
        return ResponseEntity.ok(userEventSubscriptionService.subscribe(userId, itemId));
    }

    @Override
    @DeleteMapping(value = "/{itemId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> unsubscribe(
            @PathVariable final Long userId,
            @PathVariable final Long itemId
    ) {
        userEventSubscriptionService.unsubscribe(userId, itemId);
        return ResponseEntity.ok().build();
    }
}
