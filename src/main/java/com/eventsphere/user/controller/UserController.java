package com.eventsphere.user.controller;

import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity with the list of users and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user object and HTTP status OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return ResponseEntity with the created user object and HTTP status CREATED,
     * along with the URI of the created resource in the Location header.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    /**
     * Updates an existing user.
     * Actually, I think it can be deleted, so it's deprecated even before the PR :D
     *
     * @param id   The ID of the user to update.
     * @param user The updated user object.
     * @return ResponseEntity with the updated user object and HTTP status OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.update(user));
    }

    /**
     * Updates an existing user with partial data.
     *
     * @param id      The ID of the user to update.
     * @param userDto The DTO object containing the partial user data.
     * @return ResponseEntity with the updated user object and HTTP status OK.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    /**
     * Changes the password of a user.
     *
     * @param id          The ID of the user to change the password.
     * @param passwordDto The DTO object containing the new password.
     */
    @PatchMapping("/{id}/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordDto passwordDto) {
        userService.changePassword(id, passwordDto);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
