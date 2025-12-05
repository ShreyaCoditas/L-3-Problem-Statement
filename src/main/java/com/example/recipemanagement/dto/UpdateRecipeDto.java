package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.constants.RecipeStatus;
import lombok.Data;

@Data
public class UpdateRecipeDto {
    private Long recipeId; // NOT id → recipeId for versioning
    private String title;
    private String ingredients;
    private DifficultyLevel difficultyLevel;
    private RecipeStatus recipeStatus;
    private String cookingDuration;
    private Long categoryId;
}
