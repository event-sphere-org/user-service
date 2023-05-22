package com.eventsphere.user.service;

import com.eventsphere.user.exception.UserAlreadyExistsException;
import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.exception.UserNotValidException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.UserDto;
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
            user.setCreatedAt(null);
            user.setUpdatedAt(null);

            savedUser = userRepository.save(user);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
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

    // PUT Mapping
    public User update(User user) {
        User updatedUser = null;
        User userFromDb = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        if (checkUsernameUpdate(userFromDb.getUsername(), user.getUsername()) &&
                checkEmailUpdate(userFromDb.getEmail(), user.getEmail())) {
            updatedUser = save(user);
        }

        return updatedUser;
    }

    // PATCH Mapping
    public User update(Long userId, UserDto userDto) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Username change check
        if (userDto.getUsername() != null &&
                checkUsernameUpdate(userFromDb.getUsername(), userDto.getUsername())) {
            userFromDb.setUsername(userDto.getUsername());
        }
        // Password change check
        if (userDto.getPassword() != null &&
                !userDto.getPassword().equals(userFromDb.getPassword())) {
            userFromDb.setPassword(userDto.getPassword());
        }
        // Email change check
        if (userDto.getEmail() != null &&
                checkUsernameUpdate(userFromDb.getEmail(), userDto.getEmail())) {
            userFromDb.setEmail(userDto.getEmail());
        }
        // First name change check
        if (userDto.getFirstName() != null &&
                !userDto.getFirstName().equals(userFromDb.getFirstName())) {
            userFromDb.setFirstName(userDto.getFirstName());
        }
        // Last name change check
        if (userDto.getLastName() != null &&
                !userDto.getLastName().equals(userFromDb.getLastName())) {
            userFromDb.setLastName(userDto.getLastName());
        }
        // Date of birth change check
        if (userDto.getDateOfBirth() != null &&
                userDto.getDateOfBirth().compareTo(userFromDb.getDateOfBirth()) != 0) {
            userFromDb.setDateOfBirth(userDto.getDateOfBirth());
        }

        return save(userFromDb);
    }

    public boolean checkEmailUpdate(String emailFromDb, String updatedEmail) {
        if (!updatedEmail.equals(emailFromDb) &&
                userRepository.existsByEmail(updatedEmail)) {
            throw new UserAlreadyExistsException("This email is already registered");
        }

        return true;
    }

    public boolean checkUsernameUpdate(String usernameFromDb, String updatedUsername) {
        if (!updatedUsername.equals(usernameFromDb) &&
                userRepository.existsByUsername(updatedUsername)) {
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
