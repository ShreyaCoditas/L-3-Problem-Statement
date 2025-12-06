package com.example.recipemanagement.service;

import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.LikeDetailsDto;
import com.example.recipemanagement.entity.Recipe;
import com.example.recipemanagement.entity.RecipeLike;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.exception.ResourceNotFoundException;
import com.example.recipemanagement.repository.RecipeLikeRepository;
import com.example.recipemanagement.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class LikeService {

    @Autowired
    private RecipeLikeRepository likeRepo;
    @Autowired
    private  RecipeRepository recipeRepo;

    @Transactional
    public ApiResponseDto<Void> likeRecipe(Long recipeId, User user) {

        Recipe recipe = recipeRepo.findLatestVersion(recipeId);
        if (recipe == null) {
            throw new ResourceNotFoundException("Recipe not found");
        }
        if (likeRepo.existsByUserIdAndRecipe_RecipeId(user.getId(), recipeId)) {
            return new ApiResponseDto<>(false, "You already liked this recipe", null);
        }
        RecipeLike like = new RecipeLike();
        like.setUser(user);
        like.setRecipe(recipe);
        likeRepo.save(like);
        return new ApiResponseDto<>(true, "Recipe liked successfully", null);
    }

    @Transactional
    public ApiResponseDto<Void> unlikeRecipe(Long recipeId, User user) {

        Recipe recipe = recipeRepo.findLatestVersion(recipeId);
        if (recipe == null) {
            throw new ResourceNotFoundException("Recipe not found");
        }
        likeRepo.deleteByUserIdAndRecipe_RecipeId(user.getId(), recipeId);
        return new ApiResponseDto<>(true, "Recipe unliked successfully", null);
    }


}
