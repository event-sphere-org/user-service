package com.eventsphere.user.service.implementation;

import com.eventsphere.user.exception.SubscriptionAlreadyExistsException;
import com.eventsphere.user.exception.SubscriptionNotFoundException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.UserEventSubscription;
import com.eventsphere.user.model.dto.EventDto;
import com.eventsphere.user.repository.UserEventSubscriptionRepository;
import com.eventsphere.user.service.SubscriptionService;
import com.eventsphere.user.service.UserService;
import com.eventsphere.user.service.client.EventClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
@Slf4j
public class UserEventSubscriptionService implements SubscriptionService<UserEventSubscription, EventDto> {

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
    @Override
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
     * @param userId user id
     * @param itemId event id
     * @return event subscription
     */
    @Override
    public UserEventSubscription get(Long userId, Long itemId) {
        User user = userService.get(userId);

        UserEventSubscription eventSubscription = userEventSubscriptionRepository
                .findByUserAndEventId(user, itemId)
                .orElseThrow(() -> new SubscriptionNotFoundException(
                        String.format("Can't find subscription for user %s and event %s", userId, itemId)
                ));

        eventSubscription.setEvent(eventClientService.findEvent(itemId));

        return eventSubscription;
    }

    /**
     * Subscribe user to event (create event subscription)
     *
     * @param userId user id
     * @param itemId event id to subscribe
     * @return created event subscription
     */
    @Override
    public UserEventSubscription subscribe(Long userId, Long itemId) {
        User user = userService.get(userId);

        if (userEventSubscriptionRepository.existsByUserAndEventId(user, itemId)) {
            throw new SubscriptionAlreadyExistsException(
                    String.format("User %s is already subscribed on event %s", userId, itemId)
            );
        }

        EventDto event = eventClientService.findEvent(itemId);

        UserEventSubscription eventSubscription = new UserEventSubscription(user, itemId);
        eventSubscription.setEvent(event);

        return userEventSubscriptionRepository.save(eventSubscription);
    }

    /**
     * Unsubscribe user from event (delete event subscription)
     *
     * @param userId user id
     * @param itemId event id to unsubscribe
     */
    @Override
    public void unsubscribe(Long userId, Long itemId) {
        User user = userService.get(userId);

        if (userEventSubscriptionRepository.existsByUserAndEventId(user, itemId)) {
            userEventSubscriptionRepository.deleteByUserAndEventId(user, itemId);
        } else {
            throw new SubscriptionNotFoundException(
                    String.format("Can't find subscription for user %s and event %s", userId, itemId)
            );
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.event.delete}")
    @Transactional
    public void deleteAllByEventId(Long id) {
        log.info("Received delete event message from event-service with id: " + id);
        userEventSubscriptionRepository.deleteAllByEventId(id);
        log.info("Deleted all subscriptions for event with id: " + id);
    }
}
