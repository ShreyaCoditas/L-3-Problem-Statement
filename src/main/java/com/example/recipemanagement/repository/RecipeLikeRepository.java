package com.example.recipemanagement.repository;

import com.example.recipemanagement.entity.RecipeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {

    boolean existsByUserIdAndRecipe_RecipeId(Long userId, Long recipeId);

    long countByRecipe_RecipeId(Long recipeId);

    void deleteByUserIdAndRecipe_RecipeId(Long userId, Long recipeId);

    List<RecipeLike> findByRecipe_RecipeId(Long recipeId);

    List<RecipeLike> findByUserId(Long userId);
}
