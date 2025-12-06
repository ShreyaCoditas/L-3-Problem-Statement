package com.example.recipemanagement.repository;

import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.constants.RecipeStatus;
import com.example.recipemanagement.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe,Long>, JpaSpecificationExecutor<Recipe> {

    @Query("SELECT COALESCE(MAX(r.recipeId), 0) FROM Recipe r")
    Long findMaxRecipeId();
    List<Recipe> findByTitleContainingIgnoreCase(String title);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient);
    List<Recipe> findByCategoryRecipe_Name(String categoryName);
    List<Recipe> findByCategoryRecipe_Cuisine(Cuisine cuisine);

    // Get the latest version of a recipe using recipeId
    @Query("""
           SELECT r FROM Recipe r 
           WHERE r.recipeId = :recipeId 
           ORDER BY r.version DESC LIMIT 1
           """)
    Recipe findLatestVersion(Long recipeId);

    // Get all active recipes
    List<Recipe> findByRecipeStatus(RecipeStatus status);

    // Get recipes for homepage preferred list
    List<Recipe> findByRecipeIdInAndRecipeStatus(List<Long> recipeIds, RecipeStatus status);

    @Query("""
    SELECT r FROM Recipe r 
    WHERE r.recipeStatus = 'ACTIVE'
      AND (
        LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(r.categoryRecipe.name) LIKE LOWER(CONCAT('%', :query, '%'))
      )
""")
    List<Recipe> searchPreferred(String query);




}
