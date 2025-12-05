package com.example.recipemanagement.dto;

import com.example.recipemanagement.constants.Cuisine;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryDto {
    private String name;
    private String description;
    private Cuisine cuisine;
}
