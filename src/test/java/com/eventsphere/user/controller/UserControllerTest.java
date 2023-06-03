package com.eventsphere.user.controller;

import com.eventsphere.user.exception.UserAlreadyExistsException;
import com.eventsphere.user.exception.UserNotFoundException;
import com.eventsphere.user.model.User;
import com.eventsphere.user.model.dto.ChangePasswordDto;
import com.eventsphere.user.model.dto.UserDto;
import com.eventsphere.user.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsersTest() throws Exception {
        // Given
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "user", "password1", "example@example.com"));
        when(userService.getAll()).thenReturn(users);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList").isArray())
                .andExpect(jsonPath("$._embedded.userList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userList[0].username").value("user"))
                .andExpect(jsonPath("$._embedded.userList[0].email").value("example@example.com"));
    }


    @Test
    void getExistingUserTest() throws Exception {
        // Given
        User user = new User(1L, "John", "password1", "john@example.com");
        when(userService.get(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getNonExistingUserTest() throws Exception {
        // Given
        when(userService.get(2L)).thenThrow(UserNotFoundException.class);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createValidUserTest() throws Exception {
        // Given
        User user = new User(1L, "John", "password1", "john@example.com");
        when(userService.create(user)).thenReturn(user);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(header().string("location", "http://localhost/v1/users/1"));
    }

    @Test
    void createInvalidUserTest() throws Exception {
        // Given
        User user = new User("us", "pw", "invalid-email");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("location"));
    }

    @Test
    void createUserAlreadyExistsByUsername() throws Exception {
        User user = new User(2L, "John", "password2", "john2@example.com");
        when(userService.create(user)).thenAnswer(invocation -> {
            throw new UserAlreadyExistsException("This username is already registered");
        });

        // When & Then
        // Attempt to create the second user with the existing username
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user)))
                .andExpect(status().isConflict())
                .andExpect(header().doesNotExist("location"));
    }

    @Test
    void validPatchUserTest() throws Exception {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("updateFName");
        userDto.setLastName("updateLName");
        userDto.setDateOfBirth(Date.valueOf("2001-01-01"));
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        // Configure userService mock behavior
        User updatedUser = new User(1L, "updateFName", "password1", "john@example.com");
        when(userService.update(Mockito.eq(1L), Mockito.any(UserDto.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDto)))
                .andExpect(status().isOk());

        // Verify that the update method was called with the correct arguments
        verify(userService).update(eq(1L), any(UserDto.class));
    }


    @Test
    void invalidPatchUserTest() throws Exception {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("fn");
        userDto.setLastName("ln");
        userDto.setDateOfBirth(Date.valueOf("2100-01-01"));
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validPasswordChange() throws Exception {
        // Given
        ChangePasswordDto passwordDto = new ChangePasswordDto("password1", "password2", "password2");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/users/1/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(passwordDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserSuccessful() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", userId))
                .andExpect(status().isOk());
        verify(userService).delete(userId);
    }

    @Test
    void deleteNonExistingUser() throws Exception {
        // Given
        Long userId = 1L;
        doThrow(UserNotFoundException.class)
                .when(userService)
                .delete(userId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
        verify(userService).delete(userId);
    }
}