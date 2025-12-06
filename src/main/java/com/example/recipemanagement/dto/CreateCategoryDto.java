package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.Cuisine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {
    @NotBlank(message = "category name cannot be null")
    private String name;

    @NotBlank(message = "category description cannot be empty")
    private String description;

    @NotNull(message = "cuisine cannot be null")
    private Cuisine cuisine;
}
