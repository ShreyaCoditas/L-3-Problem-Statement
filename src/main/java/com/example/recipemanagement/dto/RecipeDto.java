package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.constants.RecipeStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDto {
    private Long recipeId; // NOT id → recipeId for versioning
    private String title;
    private String ingredients;
    private DifficultyLevel difficultyLevel;
    private RecipeStatus recipeStatus;
    private String cookingDuration;
    private Long categoryId;
}
