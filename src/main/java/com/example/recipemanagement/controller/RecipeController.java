package com.example.recipemanagement.controller;

import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.dto.AddRecipeDto;
import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.RecipeDto;
import com.example.recipemanagement.dto.UpdateRecipeDto;
import com.example.recipemanagement.entity.Recipe;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.security.UserPrincipal;
import com.example.recipemanagement.service.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    @Autowired
    private  RecipeService recipeService;

    // To add a recipe
    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<Void>> addRecipe(
            @Valid @RequestBody AddRecipeDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User chef = userPrincipal.getUser();
        return ResponseEntity.ok(recipeService.addRecipe(dto, chef));
    }

    // Update Recipe → Creates new version
    @PostMapping("/update/{recipeId}")
    public ResponseEntity<ApiResponseDto<RecipeDto>> updateRecipe(
            @Valid @RequestBody UpdateRecipeDto dto,
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User chef = userPrincipal.getUser();
        return ResponseEntity.ok(recipeService.updateRecipe(dto,recipeId,chef));
    }

    @DeleteMapping("/delete/{recipeId}")
    public  ResponseEntity<ApiResponseDto<Void>> deleteRecipe(@PathVariable Long recipeId){
        ApiResponseDto<Void> response=recipeService.softDeleteRecipe(recipeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<List<RecipeDto>>> viewAllRecipes(){
        ApiResponseDto<List<RecipeDto>> response=recipeService.viewAllRecipes();
        return ResponseEntity.ok(response);
    }

    //with pagination
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<Page<RecipeDto>>> viewAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponseDto<Page<RecipeDto>> response = recipeService.viewAll(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<RecipeDto>>> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Cuisine cuisine) {
        return ResponseEntity.ok(recipeService.searchRecipes(title, ingredient, category, cuisine));
    }

    //to do(Customers can save the recipe)
//    @PostMapping("/save/customers/{recipeId}")
//    public ResponseEntity<ApiResponseDto<Void>> saveRecipe(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @PathVariable Long recipeId)
//    {
//        User customer=userPrincipal.getUser();
//        ApiResponseDto<Void> response=recipeService.saveRecipe(recipeId);
//        return ResponseEntity.ok(response);
//    }







}
