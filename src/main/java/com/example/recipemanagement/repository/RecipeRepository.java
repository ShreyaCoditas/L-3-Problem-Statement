package com.example.recipemanagement.repository;

import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    Long findMaxRecipeId();
    List<Recipe> findByTitleContainingIgnoreCase(String title);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient);
    List<Recipe> findByCategoryRecipe_Name(String categoryName);
    List<Recipe> findByCategoryRecipe_Cuisine(Cuisine cuisine);

    Integer findMaxVersion(Long recipeId);

    Recipe findLatestVersion(Long recipeId);
}
