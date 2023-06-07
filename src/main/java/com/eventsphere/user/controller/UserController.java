package com.eventsphere.user.controller;

import com.eventsphere.user.exception.BeanValidationErrorDetails;
import com.eventsphere.user.exception.ErrorDetails;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

/**
 * Controller interface for managing user-related operations.
 */
public interface UserController {

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity with the list of users and HTTP status OK.
     */
    @Operation(summary = "Retrieves a list of all users", description = "Retrieves a list of all users")
    ResponseEntity<CollectionModel<User>> getAllUsers();

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user object and HTTP status OK.
     */
    @Operation(summary = "Finds user by id", description = "Finds user by id from path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                    schema = @Schema(implementation = ErrorDetails.class)
            ))
    })
    ResponseEntity<User> getUser(@Parameter(description = "ID of user to return", required = true) final Long id);

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return ResponseEntity with the created user object and HTTP status CREATED,
     * along with the URI of the created resource in the Location header.
     */
    @Operation(summary = "Creates new user", description = "Creates new user with response body in request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data in request body",
                    content = @Content(schema = @Schema(implementation = BeanValidationErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Error saving to database",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<User> createUser(@RequestBody(description = "Request body of User to create", required = true,
            content = @Content(schema = @Schema(implementation = User.class))) final User user);

    /**
     * Updates an existing user with partial data.
     *
     * @param id      The ID of the user to update.
     * @param userDto The DTO object containing the partial user data.
     * @return ResponseEntity with the updated user object and HTTP status OK.
     */
    @Operation(summary = "Partially updates user", description = "Update user from path variable with fields in request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data in request body",
                    content = @Content(schema = @Schema(implementation = BeanValidationErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Error saving to database",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<User> updateUser(
            @Parameter(description = "ID of user to update", required = true) final Long id,
            @RequestBody(description = "UserDTO for fields updating", required = true,
                    content = @Content(schema = @Schema(implementation = UserDto.class))) final UserDto userDto
    );

    /**
     * Changes the password of a user.
     *
     * @param id          The ID of the user to change the password.
     * @param passwordDto The DTO object containing the new password.
     */
    @Operation(summary = "Changes user's password", description = "Change user's password through request body using PasswordDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "400", description = "Bad old password / new passwords don't match",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "400", description = "Password constraints don't match",
                    content = @Content(schema = @Schema(implementation = BeanValidationErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Error saving to database",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<Void> changePassword(
            @Parameter(description = "ID of user to change password", required = true) final Long id,
            @RequestBody(description = "PasswordDTO for password updating", required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordDto.class))) final ChangePasswordDto passwordDto
    );

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    @Operation(summary = "Deletes user by id", description = "Deletes user by id from path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to delete", required = true) final Long id
    );
}