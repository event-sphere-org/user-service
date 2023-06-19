package com.eventsphere.user.repository;

import com.eventsphere.user.model.User;
import com.eventsphere.user.model.UserCategorySubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCategorySubscriptionRepository extends JpaRepository<UserCategorySubscription, Long> {

    Optional<UserCategorySubscription> findByUserAndCategoryId(User user, Long categoryId);

    Page<UserCategorySubscription> findByUser(User user, Pageable pageable);

    boolean existsByUserAndCategoryId(User user, Long categoryId);

    void deleteByUserAndCategoryId(User user, Long categoryId);

    void deleteAllByUser(User user);

    void deleteAllByCategoryId(Long categoryId);
}
