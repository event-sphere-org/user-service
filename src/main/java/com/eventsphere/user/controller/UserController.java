package com.eventsphere.user.controller;

import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String GET_USER_REL = "get-user";
    private static final String CHANGE_PASSWORD_REL = "change-password";
    private static final String CREATE_USER_REL = "create-user";
    private static final String GET_ALL_USERS_REL = "get-all-users";
    private static final String SELF_REL = "self";

    private final UserService userService;

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity with the list of users and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<User>> getAllUsers() {
        List<User> users = userService.getAll();

        for (User user : users) {
            user.add(
                    linkTo(methodOn(UserController.class).getUser(user.getId()))
                            .withRel(GET_USER_REL),
                    linkTo(methodOn(UserController.class).changePassword(user.getId(), new ChangePasswordDto()))
                            .withRel(CHANGE_PASSWORD_REL)
            );
        }

        CollectionModel<User> userCollectionModel = CollectionModel.of(users);
        userCollectionModel.add(
                linkTo(methodOn(UserController.class).getAllUsers()).withRel(SELF_REL),
                linkTo(methodOn(UserController.class).createUser(new User())).withRel(CREATE_USER_REL)
        );

        return ResponseEntity.ok(userCollectionModel);
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user object and HTTP status OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.get(id);

        user.add(
                linkTo(methodOn(UserController.class).getUser(id)).withRel(SELF_REL),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserController.class).createUser(user)).withRel(CREATE_USER_REL),
                linkTo(methodOn(UserController.class).changePassword(id, new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return ResponseEntity with the created user object and HTTP status CREATED,
     * along with the URI of the created resource in the Location header.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody final User user) {
        User createdUser = userService.create(user);

        createdUser.add(
                linkTo(methodOn(UserController.class).createUser(user)).withRel(SELF_REL),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserController.class).getUser(createdUser.getId())).withRel(GET_USER_REL),
                linkTo(methodOn(UserController.class).changePassword(createdUser.getId(), new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
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
        User updatedUser = userService.update(id, userDto);

        updatedUser.add(
                linkTo(methodOn(UserController.class).updateUser(id, userDto)).withRel(SELF_REL),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserController.class).createUser(updatedUser)).withRel(CREATE_USER_REL),
                linkTo(methodOn(UserController.class).changePassword(id, new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Changes the password of a user.
     *
     * @param id          The ID of the user to change the password.
     * @param passwordDto The DTO object containing the new password.
     */
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable final Long id, @Valid @RequestBody final ChangePasswordDto passwordDto) {
        userService.changePassword(id, passwordDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
