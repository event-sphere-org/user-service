package com.eventsphere.user.service;

import com.eventsphere.user.exception.SubscriptionAlreadyExistsException;
import com.eventsphere.user.exception.SubscriptionNotFoundException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.UserEventSubscription;
import com.eventsphere.user.model.dto.EventDto;
import com.eventsphere.user.repository.UserEventSubscriptionRepository;
import com.eventsphere.user.service.client.EventClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for {@link UserEventSubscription}
 */
@Service
@RequiredArgsConstructor
public class UserEventSubscriptionService {

    private final EventClientService eventClientService;

    private final UserEventSubscriptionRepository userEventSubscriptionRepository;

    private final UserService userService;

    /**
     * Get all events subscribed by user
     *
     * @param userId user id
     * @param page   page
     * @param size   size
     * @return list of events
     */
    public List<EventDto> getAll(Long userId, int page, int size) {
        User user = userService.get(userId);
        Pageable pageable = PageRequest.of(page, size);

        Page<UserEventSubscription> eventSubscriptions = userEventSubscriptionRepository.findByUser(user, pageable);

        for (UserEventSubscription eventSubscription : eventSubscriptions) {
            eventSubscription.setEvent(eventClientService.findEvent(eventSubscription.getEventId()));
        }

        return eventSubscriptions.stream()
                .map(UserEventSubscription::getEvent)
                .toList();
    }

    /**
     * Get specific event subscription by user id and event id
     *
     * @param userId  user id
     * @param eventId event id
     * @return event subscription
     */
    public UserEventSubscription get(Long userId, Long eventId) {
        User user = userService.get(userId);

        UserEventSubscription eventSubscription = userEventSubscriptionRepository
                .findByUserAndEventId(user, eventId)
                .orElseThrow(() -> new SubscriptionNotFoundException(
                        String.format("Can't find subscription for user %s and event %s", userId, eventId)
                ));

        eventSubscription.setEvent(eventClientService.findEvent(eventId));

        return eventSubscription;
    }

    /**
     * Subscribe user to event (create event subscription)
     *
     * @param userId  user id
     * @param eventId event id to subscribe
     * @return created event subscription
     */
    public UserEventSubscription subscribeUserToEvent(Long userId, Long eventId) {
        User user = userService.get(userId);

        if (userEventSubscriptionRepository.existsByUserAndEventId(user, eventId)) {
            throw new SubscriptionAlreadyExistsException(
                    String.format("User %s is already subscribed on event %s", userId, eventId)
            );
        }

        EventDto event = eventClientService.findEvent(eventId);

        UserEventSubscription eventSubscription = new UserEventSubscription(user, eventId);
        eventSubscription.setEvent(event);

        return userEventSubscriptionRepository.save(eventSubscription);
    }

    /**
     * Unsubscribe user from event (delete event subscription)
     *
     * @param userId  user id
     * @param eventId event id to unsubscribe
     */
    @Transactional
    public void unsubscribeUserFromEvent(Long userId, Long eventId) {
        User user = userService.get(userId);

        if (userEventSubscriptionRepository.existsByUserAndEventId(user, eventId)) {
            userEventSubscriptionRepository.deleteByUserAndEventId(user, eventId);
        } else {
            throw new SubscriptionNotFoundException(
                    String.format("Can't find subscription for user %s and event %s", userId, eventId)
            );
        }
    }

    /**
     * Unsubscribe user from all events (delete all event subscriptions)
     *
     * @param userId user id
     */
    @Transactional
    public void unsubscribeUserFromAllEvents(Long userId) {
        User user = userService.get(userId);

        userEventSubscriptionRepository.deleteAllByUser(user);
    }
}
