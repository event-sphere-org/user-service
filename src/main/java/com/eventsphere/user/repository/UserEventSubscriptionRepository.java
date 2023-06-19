package com.eventsphere.user.repository;

import com.eventsphere.user.model.User;
import com.eventsphere.user.model.UserEventSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEventSubscriptionRepository extends JpaRepository<UserEventSubscription, Long> {

    Optional<UserEventSubscription> findByUserAndEventId(User user, Long eventId);

    Page<UserEventSubscription> findByUser(User user, Pageable pageable);

    boolean existsByUserAndEventId(User user, Long eventId);

    void deleteByUserAndEventId(User user, Long eventId);

    void deleteAllByUser(User user);

    void deleteAllByEventId(Long eventId);
}
