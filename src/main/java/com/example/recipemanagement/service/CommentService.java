package com.example.recipemanagement.service;

import com.example.recipemanagement.dto.AddCommentRequestDto;
import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.entity.Recipe;
import com.example.recipemanagement.entity.RecipeComment;
import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.exception.ResourceNotFoundException;
import com.example.recipemanagement.repository.RecipeCommentRepository;
import com.example.recipemanagement.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private  RecipeCommentRepository commentRepo;
    @Autowired
    private RecipeRepository recipeRepo;


    public ApiResponseDto<Void> addComment(Long recipeId, AddCommentRequestDto request, User user) {

        Recipe recipe = recipeRepo.findLatestVersion(recipeId);
        if (recipe == null) {
            throw new ResourceNotFoundException("Recipe not found!");
        }

        RecipeComment comment = new RecipeComment();
        comment.setRecipe(recipe);
        comment.setUser(user);
        comment.setComment(request.getComment());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepo.save(comment);
        return new ApiResponseDto<>(true, "Comment added successfully", null);
    }



    // DELETE COMMENT
    public ApiResponseDto<Void> deleteComment(Long commentId, User user) {

        RecipeComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete someone else's comment");
        }
        commentRepo.delete(comment);
        return new ApiResponseDto<>(true, "Comment deleted successfully", null);
    }


}
