package com.example.recipemanagement.repository;

import com.example.recipemanagement.entity.RecipeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByRecipe_RecipeId(Long recipeId);
}

