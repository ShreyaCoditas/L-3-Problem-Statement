package com.example.recipemanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private boolean success;
    private String message;
    private String error;
    private Integer statusCode;
}
