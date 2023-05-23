package com.eventsphere.user.service;

import com.eventsphere.user.exception.PasswordException;
import com.eventsphere.user.exception.UserAlreadyExistsException;
import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.exception.UserNotValidException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a list of all users.
     *
     * @return List of {@link User} objects.
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The {@link User} object.
     * @throws UserNotFoundException if the user with the given ID is not found.
     */
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Saves a user.
     *
     * @param user The {@link User} object to save.
     * @return The saved {@link User} object.
     * @throws UserNotValidException if the user data is invalid.
     */
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

    /**
     * Creates a new user.
     *
     * @param user The {@link User} object to create.
     * @return The created {@link User} object.
     * @throws UserAlreadyExistsException if a user with the same username or email already exists.
     */
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

    /**
     * Updates an existing user with partial data. (PATCH)
     *
     * @param userId  The ID of the user to update.
     * @param userDto The {@link UserDto} object containing the partial user data.
     * @return The updated {@link User} object.
     * @throws UserNotFoundException      if the user with the given ID is not found.
     * @throws UserAlreadyExistsException if a user with the updated username or email already exists.
     */
    public User update(Long userId, UserDto userDto) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Username change check
        if (userDto.getUsername() != null &&
                checkUsernameUpdate(userFromDb.getUsername(), userDto.getUsername())) {
            userFromDb.setUsername(userDto.getUsername());
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

    /**
     * Checks if the updated email is valid and not already registered.
     *
     * @param emailFromDb  The current email stored in the database.
     * @param updatedEmail The updated email to check.
     * @return true if the email is valid and not already registered, false otherwise.
     * @throws UserAlreadyExistsException if the updated email is already registered.
     */
    public boolean checkEmailUpdate(String emailFromDb, String updatedEmail) {
        if (!updatedEmail.equals(emailFromDb) &&
                userRepository.existsByEmail(updatedEmail)) {
            throw new UserAlreadyExistsException("This email is already registered");
        }

        return true;
    }

    /**
     * Checks if the updated username is valid and not already registered.
     *
     * @param usernameFromDb  The current username stored in the database.
     * @param updatedUsername The updated username to check.
     * @return true if the username is valid and not already registered, false otherwise.
     * @throws UserAlreadyExistsException if the updated username is already registered.
     */
    public boolean checkUsernameUpdate(String usernameFromDb, String updatedUsername) {
        if (!updatedUsername.equals(usernameFromDb) &&
                userRepository.existsByUsername(updatedUsername)) {
            throw new UserAlreadyExistsException("This username is already registered");
        }

        return true;
    }

    /**
     * Changes the password of a user.
     *
     * @param userId      The ID of the user to change the password.
     * @param passwordDto The {@link ChangePasswordDto} object containing the old and new passwords.
     * @throws UserNotFoundException if the user with the given ID is not found.
     * @throws PasswordException     if the old password is incorrect or the new passwords don't match.
     */
    public void changePassword(Long userId, ChangePasswordDto passwordDto) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!userFromDb.getPassword().equals(passwordDto.getOldPassword())) {
            throw new PasswordException("Incorrect old password");
        } else if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new PasswordException("Passwords don't match");
        } else {
            userFromDb.setPassword(passwordDto.getNewPassword());
            save(userFromDb);
        }
    }

    /**
     * Deletes a user.
     *
     * @param id The ID of the user to delete.
     * @throws UserNotFoundException if the user with the given ID is not found.
     */
    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}