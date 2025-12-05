package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.Roles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class LoginResponseDto {
    private Long id;
    private String name;
    private String email;
    private Roles role;
    private String token;
    private LocalDateTime createdAt;
}
