package com.eventsphere.user.service;

import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.exception.UserNotValidException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User save(User user) {
        User savedUser;

        try {
            savedUser = userRepository.save(user);
        } catch (RuntimeException ex) {
            throw new UserNotValidException("Invalid User data: " + ex.getMessage());
        }

        return savedUser;
    }

    public User update(User user) {
        User updatedUser;
        if (userRepository.existsById(user.getId())) {
            updatedUser = save(user);
        } else {
            throw new UserNotFoundException(user.getId());
        }

        return updatedUser;
    }

    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
