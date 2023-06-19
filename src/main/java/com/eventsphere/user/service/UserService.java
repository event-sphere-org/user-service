package com.eventsphere.user.service;

import com.eventsphere.user.exception.PasswordException;
import com.eventsphere.user.exception.UserAlreadyExistsException;
import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.exception.UserNotValidException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    List<User> getAll(final int page, final int size);

    User get(Long id) throws UserNotFoundException;

    User save(User user) throws UserNotValidException;

    User create(User user) throws UserAlreadyExistsException;

    User update(Long userId, UserDto userDto) throws UserNotFoundException, UserAlreadyExistsException;

    boolean checkEmailUpdate(String emailFromDb, String updatedEmail) throws UserAlreadyExistsException;

    boolean checkUsernameUpdate(String usernameFromDb, String updatedUsername) throws UserAlreadyExistsException;

    void changePassword(Long userId, ChangePasswordDto passwordDto) throws UserNotFoundException, PasswordException;

    @Transactional
    void delete(Long id) throws UserNotFoundException;
}
