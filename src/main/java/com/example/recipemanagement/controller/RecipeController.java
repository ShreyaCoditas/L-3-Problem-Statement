package com.example.recipemanagement.controller;

import com.example.recipemanagement.dto.*;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.security.UserPrincipal;
import com.example.recipemanagement.service.CommentService;
import com.example.recipemanagement.service.LikeService;
import com.example.recipemanagement.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    @Autowired
    private  RecipeService recipeService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    // To add a recipe
    @PostMapping(value= "/add" , consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseDto<Void>> addRecipe(
            @RequestPart("data") @Valid AddRecipeDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User chef = userPrincipal.getUser();
        return ResponseEntity.ok(recipeService.addRecipe(dto, file,chef));
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


    //with pagination
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDto<Page<RecipeDto>>> viewAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponseDto<Page<RecipeDto>> response = recipeService.viewAll(page, size);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/home")
    public ResponseEntity<ApiResponseDto<Page<HomeRecipeDto>>> homepageFeed(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userPrincipal.getUser();
        return ResponseEntity.ok(recipeService.homepage(user, query, page, size));
    }



    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<RecipeDto>>> searchRecipes(
            @ModelAttribute RecipeSearchRequestDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponseDto<Page<RecipeDto>> response = recipeService.searchRecipesPaginated(request, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/likes/{recipeId}")
    public ResponseEntity<ApiResponseDto<Void>> likeRecipe(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recipeId) {

        User user = userPrincipal.getUser();
        return ResponseEntity.ok(likeService.likeRecipe(recipeId, user));
    }

    @DeleteMapping("/unlikes/{recipeId}")
    public ResponseEntity<ApiResponseDto<Void>> unlikeRecipe(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recipeId) {

        User user = userPrincipal.getUser();
        return ResponseEntity.ok(likeService.unlikeRecipe(recipeId, user));
    }



    @PostMapping("/comments/{recipeId}")
    public ResponseEntity<ApiResponseDto<Void>> addComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recipeId,
            @RequestBody AddCommentRequestDto request) {

        User user = userPrincipal.getUser();
        return ResponseEntity.ok(
                commentService.addComment(recipeId, request, user)
        );
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long commentId) {

        User user = userPrincipal.getUser();
        return ResponseEntity.ok(commentService.deleteComment(commentId, user));
    }











}
