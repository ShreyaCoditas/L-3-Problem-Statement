package com.example.recipemanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Long commentId;
    private String username;
    private String comment;
    private String createdAt;
}

