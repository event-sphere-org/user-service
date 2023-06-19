package com.eventsphere.user.controller.implementation;

import com.eventsphere.user.controller.SubscriptionController;
import com.eventsphere.user.model.UserCategorySubscription;
import com.eventsphere.user.model.dto.CategoryDto;
import com.eventsphere.user.service.implementation.UserCategorySubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users/{userId}/subscriptions/categories")
@RequiredArgsConstructor
public class UserCategorySubscriptionController implements SubscriptionController<UserCategorySubscription, CategoryDto> {

    private final UserCategorySubscriptionService userCategorySubscriptionService;

    @Override
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<CategoryDto>> getAllSubscriptions(
            @PathVariable final Long userId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(userCategorySubscriptionService.getAll(userId, page, size));
    }

    @Override
    @GetMapping(value = "/{itemId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserCategorySubscription> getSubscription(
            @PathVariable final Long userId,
            @PathVariable final Long itemId
    ) {
        return ResponseEntity.ok(userCategorySubscriptionService.get(userId, itemId));
    }

    @Override
    @PostMapping(value = "/{itemId}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserCategorySubscription> subscribe(
            @PathVariable final Long userId,
            @PathVariable final Long itemId
    ) {
        return ResponseEntity.ok(userCategorySubscriptionService.subscribe(userId, itemId));
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
        userCategorySubscriptionService.unsubscribe(userId, itemId);
        return ResponseEntity.ok().build();
    }
}
