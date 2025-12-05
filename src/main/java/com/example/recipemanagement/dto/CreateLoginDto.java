package com.example.recipemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLoginDto {

    @NotBlank(message = "email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "password cannot be empty")
    private String password;
}
