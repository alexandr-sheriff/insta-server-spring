package com.example.insta.payload.request;

import com.example.insta.annotations.PasswordMatches;
import com.example.insta.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignUpRequest {

    @Email(message = "Is should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Please enter your name")
    private String firstname;

    @NotBlank(message = "Please enter your lastname")
    private String lastname;

    @NotBlank(message = "Please enter your username")
    private String username;

    @NotBlank(message = "Please enter your name")
    @Size(min = 6)
    private String password;

    @NotBlank(message = "Please enter your name")
    private String confirmPassword;
}
