package com.eventsphere.user.controller.implementation;

import com.eventsphere.user.controller.UserController;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Default implementation of {@link UserController}
 */
@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private static final String GET_USER_REL = "get-user";
    private static final String CHANGE_PASSWORD_REL = "change-password";
    private static final String CREATE_USER_REL = "create-user";
    private static final String GET_ALL_USERS_REL = "get-all-users";
    private static final String SELF_REL = "self";

    private final UserService userService;

    @Override
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CollectionModel<User>> getAllUsers() {
        List<User> users = userService.getAll();

        for (User user : users) {
            user.add(
                    linkTo(methodOn(UserControllerImpl.class).getUser(user.getId()))
                            .withRel(GET_USER_REL),
                    linkTo(methodOn(UserControllerImpl.class).changePassword(user.getId(), new ChangePasswordDto()))
                            .withRel(CHANGE_PASSWORD_REL)
            );
        }

        CollectionModel<User> userCollectionModel = CollectionModel.of(users);
        userCollectionModel.add(
                linkTo(methodOn(UserControllerImpl.class).getAllUsers()).withRel(SELF_REL),
                linkTo(methodOn(UserControllerImpl.class).createUser(new User())).withRel(CREATE_USER_REL)
        );

        return ResponseEntity.ok(userCollectionModel);
    }

    @Override
    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<User> getUser(@PathVariable final Long id) {
        User user = userService.get(id);

        user.add(
                linkTo(methodOn(UserControllerImpl.class).getUser(id)).withRel(SELF_REL),
                linkTo(methodOn(UserControllerImpl.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserControllerImpl.class).createUser(user)).withRel(CREATE_USER_REL),
                linkTo(methodOn(UserControllerImpl.class).changePassword(id, new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        return ResponseEntity.ok(user);
    }

    @Override
    @PostMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@Valid @RequestBody final User user) {
        User createdUser = userService.create(user);

        createdUser.add(
                linkTo(methodOn(UserControllerImpl.class).createUser(user)).withRel(SELF_REL),
                linkTo(methodOn(UserControllerImpl.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserControllerImpl.class).getUser(createdUser.getId())).withRel(GET_USER_REL),
                linkTo(methodOn(UserControllerImpl.class).changePassword(createdUser.getId(), new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @Override
    @PatchMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<User> updateUser(
            @PathVariable final Long id,
            @Valid @RequestBody final UserDto userDto
    ) {
        User updatedUser = userService.update(id, userDto);

        updatedUser.add(
                linkTo(methodOn(UserControllerImpl.class).updateUser(id, userDto)).withRel(SELF_REL),
                linkTo(methodOn(UserControllerImpl.class).getAllUsers()).withRel(GET_ALL_USERS_REL),
                linkTo(methodOn(UserControllerImpl.class).createUser(updatedUser)).withRel(CREATE_USER_REL),
                linkTo(methodOn(UserControllerImpl.class).changePassword(id, new ChangePasswordDto())).withRel(CHANGE_PASSWORD_REL)
        );

        return ResponseEntity.ok(updatedUser);
    }

    @Override
    @PatchMapping(value = "/{id}/change-password", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> changePassword(
            @PathVariable final Long id,
            @Valid @RequestBody final ChangePasswordDto passwordDto
    ) {
        userService.changePassword(id, passwordDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
