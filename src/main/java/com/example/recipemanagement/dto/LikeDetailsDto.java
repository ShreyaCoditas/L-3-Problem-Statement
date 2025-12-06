package com.example.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LikeDetailsDto {
    private long likeCount;
    private List<String> likers;
}
