package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.constants.RecipeStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class HomeRecipeDto {

    private Long recipeId;
    private String title;
    private String ingredients;
    private DifficultyLevel difficultyLevel;
    private RecipeStatus recipeStatus;
    private String cookingDuration;

    private long likeCount;
    private long commentCount;

    private List<String> likers;
    private List<CommentDto> comments;
}

