package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRegisterDto {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "role is required")
    private Roles role;
}
