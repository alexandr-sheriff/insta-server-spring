package com.example.insta.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    private String username;
    private String bio;

}
