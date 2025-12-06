package com.example.recipemanagement.service;


import com.example.recipemanagement.constants.RecipeStatus;
import com.example.recipemanagement.dto.*;
import com.example.recipemanagement.entity.CategoryRecipe;
import com.example.recipemanagement.entity.Recipe;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.exception.ResourceNotFoundException;
import com.example.recipemanagement.repository.CategoryRecipeRepository;
import com.example.recipemanagement.repository.RecipeCommentRepository;
import com.example.recipemanagement.repository.RecipeLikeRepository;
import com.example.recipemanagement.repository.RecipeRepository;
import com.example.recipemanagement.specification.RecipeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class  RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRecipeRepository categoryRepo;
    private final FileUploadService fileUploadService;
    private final AuditLogService auditLogService;
    private final RecipeLikeRepository likeRepo;
    private final RecipeCommentRepository commentRepo;


    // create new recipe-version 1

    public ApiResponseDto<Void> addRecipe(AddRecipeDto dto,MultipartFile file, User chef) {

        CategoryRecipe category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Long maxRecipeId = recipeRepository.findMaxRecipeId();
        Long recipeId = maxRecipeId + 1;

        Recipe recipe = new Recipe();
        recipe.setVersion(1);
        recipe.setRecipeId(recipeId);
        recipe.setTitle(dto.getTitle());
        recipe.setIngredients(dto.getIngredients());
        recipe.setDifficultyLevel(dto.getDifficultyLevel());
        recipe.setRecipeStatus(RecipeStatus.ACTIVE);
        recipe.setCookingDuration(dto.getCookingDuration());
        recipe.setChef(chef);
        recipe.setCategoryRecipe(category);

        // Upload File
        if (file != null) {
            validateFile(file);   // validate file types

            Map uploadResult = fileUploadService.uploadFile(file);
            recipe.setFileUrl(uploadResult.get("url").toString());
            recipe.setFileName(file.getOriginalFilename());
            recipe.setFileType(file.getContentType());
            recipe.setFileSize(file.getSize());
        }

        recipeRepository.save(recipe);
        auditLogService.logAction(chef.getId(), recipe.getRecipeId(), "CREATE_RECIPE");

        return new ApiResponseDto<>(true, "Recipe added successfully", null);
    }


    public ApiResponseDto<RecipeDto> updateRecipe(UpdateRecipeDto dto, Long recipeId,User chef) {

        Recipe latestRecipe = recipeRepository.findLatestVersion(dto.getRecipeId());
        if (latestRecipe == null) {
            throw new ResourceNotFoundException("Recipe not found!");
        }

        if (!latestRecipe.getChef().getId().equals(chef.getId())) {
            throw new AccessDeniedException("You are not allowed to update this recipe");
        }

        CategoryRecipe category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Recipe newVersion = new Recipe();
        newVersion.setRecipeId(dto.getRecipeId());
        newVersion.setVersion(latestRecipe.getVersion() + 1);
        newVersion.setTitle(dto.getTitle());
        newVersion.setIngredients(dto.getIngredients());
        newVersion.setDifficultyLevel(dto.getDifficultyLevel());
        //newVersion.setRecipeStatus(dto.getRecipeStatus());
        newVersion.setCookingDuration(dto.getCookingDuration());
        newVersion.setChef(chef);
        newVersion.setCategoryRecipe(category);

       Recipe saved= recipeRepository.save(newVersion);
        auditLogService.logAction(chef.getId(), dto.getRecipeId(), "UPDATE_RECIPE");

        return new ApiResponseDto<>(true, "New recipe version created", mapToDto(saved));
    }

    public ApiResponseDto<Void> softDeleteRecipe(Long recipeId) {
        Recipe recipe=recipeRepository.findById(recipeId)
                .orElseThrow(()->new ResourceNotFoundException("Recipe Not Found"));
        recipe.setRecipeStatus(RecipeStatus.INACTIVE);
        recipeRepository.save(recipe);
        auditLogService.logAction(null, recipeId, "DELETE_RECIPE");
        return new ApiResponseDto<>(true,"Soft deleted recipe successfully",null);
    }


    //with pagination
    public ApiResponseDto<Page<RecipeDto>> viewAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);
        Page<RecipeDto> dtoPage = recipePage.map(this::mapToDto);
        return new ApiResponseDto<>(true, "All recipes fetched", dtoPage);
    }

    // Homepage feed: preferred recipes first (search/liked/commented), then other active recipes
    public ApiResponseDto<Page<HomeRecipeDto>> homepage(User user, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Search-based preference
        List<Long> searchedRecipeIds = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            searchedRecipeIds = recipeRepository.searchPreferred(query)
                    .stream()
                    .map(r -> r.getRecipeId())
                    .distinct()
                    .toList();
        }

        // Liked recipes
        List<Long> likedRecipeIds = likeRepo.findByUserId(user.getId())
                .stream()
                .map(like -> like.getRecipe().getRecipeId())
                .distinct()
                .toList();

        //  Commented recipes
        List<Long> commentedRecipeIds = commentRepo.findAll()
                .stream()
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .map(c -> c.getRecipe().getRecipeId())
                .distinct()
                .toList();

        //  Merge all preferred IDs
        List<Long> preferredIds = new ArrayList<>();
        preferredIds.addAll(searchedRecipeIds);
        preferredIds.addAll(likedRecipeIds);
        preferredIds.addAll(commentedRecipeIds);

        preferredIds = preferredIds.stream().distinct().toList();
        final List<Long> preferred = preferredIds;

        //  Fetch preferred recipes
        List<Recipe> preferredRecipes = preferredIds.isEmpty()
                ? List.of()
                : recipeRepository.findByRecipeIdInAndRecipeStatus(preferredIds, RecipeStatus.ACTIVE);

        //  Filter latest versions for preferred
        preferredRecipes = getLatestVersions(preferredRecipes);

        // Fetch remaining active recipes
        List<Recipe> otherRecipes = recipeRepository.findByRecipeStatus(RecipeStatus.ACTIVE)
                .stream()
                .filter(r -> !preferred.contains(r.getRecipeId()))
                .toList();

        // Filter latest versions for others
        otherRecipes = getLatestVersions(otherRecipes);

        //  Combine lists
        List<Recipe> finalList = new ArrayList<>();
        finalList.addAll(preferredRecipes);
        finalList.addAll(otherRecipes);

        //  Pagination
        int start = Math.min((int) pageable.getOffset(), finalList.size());
        int end = Math.min(start + pageable.getPageSize(), finalList.size());

        List<HomeRecipeDto> dtoList = finalList.subList(start, end)
                .stream()
                .map(this::mapToHomeDto)
                .toList();

        Page<HomeRecipeDto> pageResult = new PageImpl<>(dtoList, pageable, finalList.size());

        return new ApiResponseDto<>(true, "Homepage feed fetched", pageResult);
    }


    //search with pagination
    public ApiResponseDto<Page<RecipeDto>> searchRecipesPaginated(RecipeSearchRequestDto request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = recipeRepository.findAll(
                RecipeSpecification.search(request.getTitle(), request.getIngredient(), request.getCategory(), request.getCuisine(), request.getDifficultyLevel()), pageable);
        Page<RecipeDto> dtoPage = recipePage.map(this::mapToDto);
        return new ApiResponseDto<>(true, "Filtered recipes fetched", dtoPage);
    }



    private void validateFile(MultipartFile file) {
        String type = file.getContentType();
        long size = file.getSize();

        List<String> allowed = List.of(
                "application/pdf",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "image/jpeg",
                "image/png",
                "text/plain"
        );

        if (!allowed.contains(type)) {
            throw new IllegalArgumentException("Invalid file type! Upload PDF/DOCX/JPG/PNG/TXT only.");
        }

        if (size > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File too large! Max 10MB allowed.");
        }
    }



    private RecipeDto mapToDto(Recipe recipe){
        return RecipeDto.builder()
                .recipeId(recipe.getRecipeId())
                .title(recipe.getTitle())
                .ingredients(recipe.getIngredients())
                .difficultyLevel(recipe.getDifficultyLevel())
                .recipeStatus(RecipeStatus.ACTIVE)
                .cookingDuration(recipe.getCookingDuration())
                .build();

    }
    private HomeRecipeDto mapToHomeDto(Recipe recipe) {

        long likeCount = likeRepo.countByRecipe_RecipeId(recipe.getRecipeId());

        List<String> likers = likeRepo.findByRecipe_RecipeId(recipe.getRecipeId())
                .stream()
                .map(like -> like.getUser().getName())
                .toList();

        List<CommentDto> comments = commentRepo.findByRecipe_RecipeId(recipe.getRecipeId())
                .stream()
                .map(c -> CommentDto.builder()
                        .commentId(c.getId())
                        .username(c.getUser().getName())
                        .comment(c.getComment())
                        .createdAt(c.getCreatedAt().toString())
                        .build())
                .toList();

        return HomeRecipeDto.builder()
                .recipeId(recipe.getRecipeId())
                .title(recipe.getTitle())
                .ingredients(recipe.getIngredients())
                .difficultyLevel(recipe.getDifficultyLevel())
                .recipeStatus(recipe.getRecipeStatus())
                .cookingDuration(recipe.getCookingDuration())
                .likeCount(likeCount)
                .commentCount((long) comments.size())
                .likers(likers)
                .comments(comments)
                .build();
    }

    private List<Recipe> getLatestVersions(List<Recipe> recipes) {
        Map<Long, Recipe> latestMap = new HashMap<>();

        for (Recipe r : recipes) {
            latestMap.compute(r.getRecipeId(), (id, existing) ->
                    (existing == null || r.getVersion() > existing.getVersion()) ? r : existing
            );
        }

        return new ArrayList<>(latestMap.values());
    }



}

