package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.constants.RecipeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddRecipeDto {

    @NotNull
    @Positive
    private Long recipeId;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "ingredients are required")
    private String ingredients;

    @NotBlank(message = "Choose your difficulty level")
    private DifficultyLevel difficultyLevel;

    //private RecipeStatus recipeStatus;
    @NotBlank(message = "cooking duration is required")
    private String cookingDuration;

    @NotNull(message = "Category Id cannot be null")
    @Positive
    private Long categoryId;
}
