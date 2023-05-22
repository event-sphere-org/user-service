package com.eventsphere.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class UserDto {

    @Size(min = 3, message = "Username must be at least 3 characters")
    @Size(max = 50, message = "Username must be no more than 50 characters")
    private String username;

    @Email(message = "Provide a valid email")
    private String email;

    @Size(min = 3, message = "First name must be at least 3 characters")
    @Size(max = 50, message = "First name must be no more than 50 characters")
    private String firstName;

    @Size(min = 3, message = "Last name must be at least 3 characters")
    @Size(max = 50, message = "Last name must be no more than 50 characters")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    private Date dateOfBirth;
}
