package com.eventsphere.user.service;

import com.eventsphere.user.exception.UserAlreadyExistsException;
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

    public User create(User user) {
        User createdUser;

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("This username is already registered");
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("This email is already registered");
        } else {
            createdUser = save(user);
        }

        return createdUser;
    }

    public User update(User user) {
        User updatedUser = null;
        User userFromDb = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        if (checkUsernameUpdate(userFromDb, user) && checkEmailUpdate(userFromDb, user)) {
            updatedUser = save(user);
        }

        return updatedUser;
    }

    public boolean checkEmailUpdate(User userFromDb, User updatedUser) {
        if (!updatedUser.getEmail().equals(userFromDb.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new UserAlreadyExistsException("This email is already registered");
        }

        return true;
    }

    public boolean checkUsernameUpdate(User userFromDb, User updatedUser) {
        if (!updatedUser.getUsername().equals(userFromDb.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new UserAlreadyExistsException("This username is already registered");
        }

        return true;
    }

    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
