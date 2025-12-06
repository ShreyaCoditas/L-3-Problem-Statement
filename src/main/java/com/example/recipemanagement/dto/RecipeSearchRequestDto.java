package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.constants.DifficultyLevel;
import lombok.Data;

@Data
public class RecipeSearchRequestDto {
    private String title;
    private String ingredient;
    private String category;
    private Cuisine cuisine;
    private DifficultyLevel difficultyLevel;  // NEW FILTER
}
