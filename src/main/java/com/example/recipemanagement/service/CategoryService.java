package com.example.recipemanagement.service;

import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.CategoryResponseDto;
import com.example.recipemanagement.dto.CreateCategoryDto;
import com.example.recipemanagement.dto.UpdateCategoryDto;
import com.example.recipemanagement.entity.CategoryRecipe;
import com.example.recipemanagement.exception.ResourceAlreadyExistsException;
import com.example.recipemanagement.repository.CategoryRecipeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRecipeRepository categoryRecipeRepository;

    public ApiResponseDto<Void> addCategory(@Valid CreateCategoryDto createCategoryDto) {

//        if (categoryRecipeRepository.findByNameIgnoreCase(name).isPresent())
//            throw new ResourceAlreadyExistsException("name already exists");

        CategoryRecipe category=new CategoryRecipe();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());
        category.setCuisine(createCategoryDto.getCuisine());
        categoryRecipeRepository.save(category);
        return new ApiResponseDto<>(true,"category created successfully",null);
    }


    public ApiResponseDto<CategoryResponseDto> updateCategory(@Valid UpdateCategoryDto updateCategoryDto, Long categoryId) {
        CategoryRecipe categoryRecipe=categoryRecipeRepository.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Category Id not found"));

       categoryRecipe.setName(updateCategoryDto.getName());
       categoryRecipe.setDescription(updateCategoryDto.getDescription());
       categoryRecipe.setCuisine(updateCategoryDto.getCuisine());
       categoryRecipeRepository.save(categoryRecipe);
       return new ApiResponseDto<>(true,"category updated successfully",mapToDto(categoryRecipe));
    }

    private CategoryResponseDto mapToDto(CategoryRecipe categoryRecipe){
        return CategoryResponseDto.builder()
                .categoryId(categoryRecipe.getId())
                .name(categoryRecipe.getName())
                .description(categoryRecipe.getDescription())
                .cuisine(categoryRecipe.getCuisine())
                .build();
    }


}
