package com.example.recipemanagement.repository;

import com.example.recipemanagement.entity.CategoryRecipe;
import com.example.recipemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRecipeRepository extends JpaRepository<CategoryRecipe,Long> {
    Optional<CategoryRecipe> findByNameIgnoreCase(String name);
}
