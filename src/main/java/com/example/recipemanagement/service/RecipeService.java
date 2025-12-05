package com.example.recipemanagement.service;


import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.constants.RecipeStatus;
import com.example.recipemanagement.constants.Roles;
import com.example.recipemanagement.dto.AddRecipeDto;
import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.RecipeDto;
import com.example.recipemanagement.dto.UpdateRecipeDto;
import com.example.recipemanagement.entity.CategoryRecipe;
import com.example.recipemanagement.entity.Recipe;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.exception.ResourceNotFoundException;
import com.example.recipemanagement.repository.CategoryRecipeRepository;
import com.example.recipemanagement.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRecipeRepository categoryRepo;

    // create new recipe-version 1

    public ApiResponseDto<Void> addRecipe(AddRecipeDto dto, User chef) {

        CategoryRecipe category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Recipe recipe = new Recipe();
        recipe.setVersion(1);
        recipe.setRecipeId(dto.getRecipeId());
        recipe.setTitle(dto.getTitle());
        recipe.setIngredients(dto.getIngredients());
        recipe.setDifficultyLevel(dto.getDifficultyLevel());
        recipe.setRecipeStatus(RecipeStatus.ACTIVE);
        recipe.setCookingDuration(dto.getCookingDuration());
        recipe.setChef(chef);
        recipe.setCategoryRecipe(category);

        recipeRepository.save(recipe);

        return new ApiResponseDto<>(true, "Recipe added successfully", null);
    }


    public ApiResponseDto<RecipeDto> updateRecipe(UpdateRecipeDto dto, Long recipeId,User chef) {

        Integer latest = recipeRepository.findMaxVersion(dto.getRecipeId());
        if (latest == 0) throw new ResourceNotFoundException("Recipe not found!");

        Recipe latestRecipe = recipeRepository.findLatestVersion(dto.getRecipeId());

        if (!latestRecipe.getChef().getId().equals(chef.getId())) {
            throw new AccessDeniedException("You are not allowed to update this recipe");
        }

        CategoryRecipe category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Recipe newVersion = new Recipe();
        newVersion.setRecipeId(dto.getRecipeId());
        newVersion.setVersion(latest + 1);
        newVersion.setTitle(dto.getTitle());
        newVersion.setIngredients(dto.getIngredients());
        newVersion.setDifficultyLevel(dto.getDifficultyLevel());
        newVersion.setRecipeStatus(dto.getRecipeStatus());
        newVersion.setCookingDuration(dto.getCookingDuration());
        newVersion.setChef(chef);
        newVersion.setCategoryRecipe(category);

       Recipe saved= recipeRepository.save(newVersion);

        return new ApiResponseDto<>(true, "New recipe version created", mapToDto(saved));
    }

    public ApiResponseDto<Void> softDeleteRecipe(Long recipeId) {
        Recipe recipe=recipeRepository.findById(recipeId)
                .orElseThrow(()->new ResourceNotFoundException("Recipe Not Found"));
        //recipeRepository.deleteById(recipeId);
        recipe.setRecipeStatus(RecipeStatus.INACTIVE);
        recipeRepository.save(recipe);
        return new ApiResponseDto<>(true,"Soft deleted recipe successfully",null);
    }

    public ApiResponseDto<List<RecipeDto>> viewAllRecipes() {
        List<Recipe> recipes=new ArrayList<>();
        List<RecipeDto> recipeDtos=recipes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ApiResponseDto<>(true,"recipes are fetched",recipeDtos);
    }


    public ApiResponseDto<Page<RecipeDto>> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        Page<RecipeDto> dtoPage = recipePage.map(this::mapToDto);
        return new ApiResponseDto<>(true, "All recipes fetched", dtoPage);
    }


    public ApiResponseDto<List<RecipeDto>> searchRecipes(String title, String ingredient, String category, Cuisine cuisine) {
        List<Recipe> recipes = new ArrayList<>();
        if (title != null) {
            recipes.addAll(recipeRepository.findByTitleContainingIgnoreCase(title));
        }
        if (ingredient != null) {
            recipes.addAll(recipeRepository.findByIngredientsContainingIgnoreCase(ingredient));
        }
        if (category != null) {
            recipes.addAll(recipeRepository.findByCategoryRecipe_Name(category));
        }
        if (cuisine != null) {
            recipes.addAll(recipeRepository.findByCategoryRecipe_Cuisine(cuisine));
        }
        List<RecipeDto> recipeDtos = recipes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ApiResponseDto<>(true, "Filtered recipes fetched", recipeDtos);
    }



    private RecipeDto mapToDto(Recipe recipe){
        return RecipeDto.builder()
                .recipeId(recipe.getRecipeId())
                .title(recipe.getTitle())
                .ingredients(recipe.getIngredients())
                .difficultyLevel(recipe.getDifficultyLevel())
                .recipeStatus(recipe.getRecipeStatus())
                .cookingDuration(recipe.getCookingDuration())
                .build();

    }

}

