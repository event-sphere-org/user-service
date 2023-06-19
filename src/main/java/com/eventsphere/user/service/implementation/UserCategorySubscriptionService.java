package com.eventsphere.user.service.implementation;

import com.eventsphere.user.exception.SubscriptionAlreadyExistsException;
import com.eventsphere.user.exception.SubscriptionNotFoundException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.UserCategorySubscription;
import com.eventsphere.user.model.dto.CategoryDto;
import com.eventsphere.user.repository.UserCategorySubscriptionRepository;
import com.eventsphere.user.service.SubscriptionService;
import com.eventsphere.user.service.UserService;
import com.eventsphere.user.service.client.CategoryClientService;
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
 * Service for {@link UserCategorySubscription}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCategorySubscriptionService implements SubscriptionService<UserCategorySubscription, CategoryDto> {

    private final UserCategorySubscriptionRepository userCategorySubscriptionRepository;

    private final CategoryClientService categoryClientService;

    private final UserService userService;

    /**
     * Get all categories subscribed by user
     *
     * @param userId user id
     * @param page   page
     * @param size   size
     * @return list of categories
     */
    @Override
    public List<CategoryDto> getAll(Long userId, int page, int size) {
        User user = userService.get(userId);
        Pageable pageable = PageRequest.of(page, size);

        Page<UserCategorySubscription> categorySubscriptions = userCategorySubscriptionRepository.findByUser(user, pageable);

        for (UserCategorySubscription categorySubscription : categorySubscriptions) {
            categorySubscription.setCategory(categoryClientService.findCategory(categorySubscription.getCategoryId()));
        }

        return categorySubscriptions.stream()
                .map(UserCategorySubscription::getCategory)
                .toList();
    }

    /**
     * Get specific category subscription by user id and category id
     *
     * @param userId user id
     * @param itemId event id
     * @return category subscription
     */
    @Override
    public UserCategorySubscription get(Long userId, Long itemId) {
        User user = userService.get(userId);

        UserCategorySubscription categorySubscription = userCategorySubscriptionRepository
                .findByUserAndCategoryId(user, itemId)
                .orElseThrow(() -> new SubscriptionNotFoundException(
                        String.format("Can't find subscription for user %s and category %s", userId, itemId)
                ));

        categorySubscription.setCategory(categoryClientService.findCategory(itemId));

        return categorySubscription;
    }

    /**
     * Subscribe user to category (create category subscription)
     *
     * @param userId user id
     * @param itemId category id to subscribe
     * @return created category subscription
     */
    @Override
    public UserCategorySubscription subscribe(Long userId, Long itemId) {
        User user = userService.get(userId);

        if (userCategorySubscriptionRepository.existsByUserAndCategoryId(user, itemId)) {
            throw new SubscriptionAlreadyExistsException(
                    String.format("User %s is already subscribed on category %s", userId, itemId)
            );
        }

        CategoryDto category = categoryClientService.findCategory(itemId);

        UserCategorySubscription categorySubscription = new UserCategorySubscription(user, itemId);
        categorySubscription.setCategory(category);

        return userCategorySubscriptionRepository.save(categorySubscription);
    }

    /**
     * Unsubscribe user from category (delete category subscription)
     *
     * @param userId user id
     * @param itemId category id to unsubscribe
     */
    @Override
    public void unsubscribe(Long userId, Long itemId) {
        User user = userService.get(userId);

        if (userCategorySubscriptionRepository.existsByUserAndCategoryId(user, itemId)) {
            userCategorySubscriptionRepository.deleteByUserAndCategoryId(user, itemId);
        } else {
            throw new SubscriptionNotFoundException(
                    String.format("Can't find subscription for user %s and category %s", userId, itemId)
            );
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.category.delete}")
    @Transactional
    public void deleteAllByCategoryId(Long id) {
        log.info("Received delete category message from event-service with id: " + id);
        userCategorySubscriptionRepository.deleteAllByCategoryId(id);
        log.info("Deleted all subscriptions for category with id: " + id);
    }
}
