package com.eventsphere.user.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionService<T, S> {

    List<S> getAll(Long userId, int page, int size);

    T get(Long userId, Long itemId);

    T subscribe(Long userId, Long itemId);

    @Transactional
    void unsubscribe(Long userId, Long itemId);
}
