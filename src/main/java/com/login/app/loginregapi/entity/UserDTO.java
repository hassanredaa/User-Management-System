package com.login.app.loginregapi.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Email(message = "Username must be a valid email address")
    private String username;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character")
    @NotBlank(message = "Password should not be empty")
    private String password;

    @Pattern(regexp = "^[0-9]{14}$", message = "NID must be exactly 14 digits")
    private String nid;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    private LocalDate dateOfBirth;

    private String phoneNumber;
    private String role = "USER";
}
