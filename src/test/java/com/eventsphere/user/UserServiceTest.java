package com.eventsphere.user;

import com.eventsphere.user.exception.PasswordException;
import com.eventsphere.user.exception.UserAlreadyExistsException;
import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.exception.UserNotValidException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.repository.UserRepository;
import com.eventsphere.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getAllShouldReturnAllUsers() {
        // Given
        List<User> expectedUsers = Arrays.asList(
                new User(1L, "user1", "user1@example.com"),
                new User(2L, "user2", "user2@example.com")
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> actualUsers = userService.getAll();

        // Then
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getValidIdShouldReturnUser() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User expectedUser = new User(userId, "user1", "user1@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // When
        User actualUser = userService.get(userId);

        // Then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getInvalidIdShouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.get(userId));
    }

    @Test
    void saveValidUserShouldSaveUser() throws UserNotValidException {
        // Given
        User userToSave = new User("user1", "user1@example.com");
        User expectedSavedUser = new User(1L, "user1", "user1@example.com");
        when(userRepository.save(userToSave)).thenReturn(expectedSavedUser);

        // When
        User actualSavedUser = userService.save(userToSave);

        // Then
        assertEquals(expectedSavedUser, actualSavedUser);
        assertNull(actualSavedUser.getCreatedAt());
        assertNull(actualSavedUser.getUpdatedAt());
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void createNewUserShouldCreateUser() throws UserAlreadyExistsException, UserNotValidException {
        // Given
        User newUser = new User("user1", "user1@example.com");
        when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);

        // When
        User createdUser = userService.create(newUser);

        // Then
        assertEquals(newUser, createdUser);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void createUserWithExistingUsernameShouldThrowUserAlreadyExistsException() {
        // Given
        User existingUser = new User("user1", "user1@example.com");
        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(existingUser));
    }

    @Test
    void createUserWithExistingEmailShouldThrowUserAlreadyExistsException() {
        // Given
        User existingUser = new User("user1", "user1@example.com");
        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(existingUser.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(existingUser));
    }

    @Test
    void updateExistingUserShouldUpdateUser() throws UserNotFoundException, UserAlreadyExistsException {
        // Given
        Long userId = 1L;
        User existingUser = new User(userId, "user1", "user1@example.com");
        User updatedUser = new User(userId, "user1updated", "user1updated@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(updatedUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(false);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        // When
        User actualUpdatedUser = userService.update(updatedUser);

        // Then
        assertEquals(updatedUser, actualUpdatedUser);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateNonExistingUserShouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        User nonExistingUser = new User(userId, "user1", "user1@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.update(nonExistingUser));
    }

    @Test
    void updateUserWithExistingUsernameShouldThrowUserAlreadyExistsException() {
        // Given
        Long userId = 1L;
        User existingUser = new User(userId, "user1", "user1@example.com");
        User updatedUser = new User(userId, "user2", "user1@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(updatedUser.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.update(updatedUser));
    }

    @Test
    void updateUserWithExistingEmailShouldThrowUserAlreadyExistsException() {
        // Given
        Long userId = 1L;
        User existingUser = new User(userId, "user1", "user1@example.com");
        User updatedUser = new User(userId, "user1", "user2@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.update(updatedUser));
    }

    @Test
    void updatePartialUserDataShouldUpdateUserFields() throws UserNotFoundException, UserAlreadyExistsException {
        // Given
        Long userId = 1L;
        User existingUser = new User(userId, "user1", "user1@example.com");

        UserDto userDto = new UserDto();
        userDto.setUsername("user1updated");
        userDto.setEmail("user1updated@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setDateOfBirth(Date.valueOf("1990-01-01"));

        User expectedUpdatedUser = new User(userId, "user1updated", "user1updated@example.com");
        expectedUpdatedUser.setFirstName("John");
        expectedUpdatedUser.setLastName("Doe");
        expectedUpdatedUser.setDateOfBirth(Date.valueOf("1990-01-01"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.save(expectedUpdatedUser)).thenReturn(expectedUpdatedUser);

        // When
        User actualUpdatedUser = userService.update(userId, userDto);

        // Then
        assertEquals(expectedUpdatedUser, actualUpdatedUser);
        verify(userRepository, times(1)).save(expectedUpdatedUser);
    }

    @Test
    void checkEmailUpdateValidEmailShouldReturnTrue() throws UserAlreadyExistsException {
        // Given
        String emailFromDb = "user1@example.com";
        String updatedEmail = "user1updated@example.com";
        when(userRepository.existsByEmail(updatedEmail)).thenReturn(false);

        // When
        boolean result = userService.checkEmailUpdate(emailFromDb, updatedEmail);

        // Then
        assertTrue(result);
    }

    @Test
    void checkEmailUpdateSameEmailShouldReturnTrue() throws UserAlreadyExistsException {
        // Given
        String emailFromDb = "user1@example.com";
        String updatedEmail = "user1@example.com";

        // When
        boolean result = userService.checkEmailUpdate(emailFromDb, updatedEmail);

        // Then
        assertTrue(result);
    }

    @Test
    void checkEmailUpdateInvalidEmailShouldThrowUserAlreadyExistsException() {
        // Given
        String emailFromDb = "user1@example.com";
        String updatedEmail = "user1updated@example.com";
        when(userRepository.existsByEmail(updatedEmail)).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.checkEmailUpdate(emailFromDb, updatedEmail));
    }

    @Test
    void checkUsernameUpdateValidUsernameShouldReturnTrue() throws UserAlreadyExistsException {
        // Given
        String usernameFromDb = "user1";
        String updatedUsername = "user1updated";
        when(userRepository.existsByUsername(updatedUsername)).thenReturn(false);

        // When
        boolean result = userService.checkUsernameUpdate(usernameFromDb, updatedUsername);

        // Then
        assertTrue(result);
    }

    @Test
    void checkUsernameUpdateSameUsernameShouldReturnTrue() throws UserAlreadyExistsException {
        // Given
        String usernameFromDb = "user1";
        String updatedUsername = "user1";

        // When
        boolean result = userService.checkUsernameUpdate(usernameFromDb, updatedUsername);

        // Then
        assertTrue(result);
    }

    @Test
    void checkUsernameUpdateInvalidUsernameShouldThrowUserAlreadyExistsException() {
        // Given
        String usernameFromDb = "user1";
        String updatedUsername = "user1updated";
        when(userRepository.existsByUsername(updatedUsername)).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.checkUsernameUpdate(usernameFromDb, updatedUsername));
    }

    @Test
    void changePasswordValidDataShouldChangePassword() throws UserNotFoundException, PasswordException {
        // Given
        Long userId = 1L;

        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        String confirmPassword = "newpassword";
        ChangePasswordDto passwordDto = new ChangePasswordDto(oldPassword, newPassword, confirmPassword);

        User userFromDb = new User(userId, "user1", "user1@example.com");
        userFromDb.setPassword(oldPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));
        when(userRepository.save(userFromDb)).thenReturn(userFromDb);

        // When
        userService.changePassword(userId, passwordDto);

        // Then
        verify(userRepository, times(1)).save(userFromDb);
    }

    @Test
    void changePasswordInvalidOldPasswordShouldThrowPasswordException() {
        // Given
        Long userId = 1L;

        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        String confirmPassword = "newpassword";
        ChangePasswordDto passwordDto = new ChangePasswordDto(oldPassword, newPassword, confirmPassword);

        User userFromDb = new User(userId, "user1", "user1@example.com");
        userFromDb.setPassword("differentpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));

        // When & Then
        assertThrows(PasswordException.class, () -> userService.changePassword(userId, passwordDto));
        verify(userRepository, never()).save(userFromDb);
    }

    @Test
    void changePasswordPasswordsDoNotMatchShouldThrowPasswordException() {
        // Given
        Long userId = 1L;

        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        String confirmPassword = "differentpassword";
        ChangePasswordDto passwordDto = new ChangePasswordDto(oldPassword, newPassword, confirmPassword);

        User userFromDb = new User(userId, "user1", "user1@example.com");

        userFromDb.setPassword(oldPassword);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));

        // When & Then
        assertThrows(PasswordException.class, () -> userService.changePassword(userId, passwordDto));
        verify(userRepository, never()).save(userFromDb);
    }

    @Test
    void deleteUserNonExistingIdShouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, never()).delete(any());
    }
}
