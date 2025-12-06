package com.example.recipemanagement.controller;


import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.CategoryResponseDto;
import com.example.recipemanagement.dto.CreateCategoryDto;
import com.example.recipemanagement.dto.UpdateCategoryDto;
import com.example.recipemanagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<Void>> addCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto){
        ApiResponseDto<Void> response=categoryService.addCategory(createCategoryDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateCategory(
            @Valid @RequestBody UpdateCategoryDto updateCategoryDto,
          @PathVariable Long categoryId){
        ApiResponseDto<CategoryResponseDto> response=categoryService.updateCategory(updateCategoryDto,categoryId);
        return ResponseEntity.ok(response);
    }
}
