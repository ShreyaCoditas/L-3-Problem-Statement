package com.example.recipemanagement.entity;

import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.constants.RecipeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="recipeId")
    private Long recipeId;

   // @Column(name="image")
   // private MultipartFile image;

    @Column(name="title")
    private String title;

    @Column(name="ingredients")
    private String ingredients;

    @Enumerated(EnumType.STRING)
    @Column(name="difficulty_level")
    private DifficultyLevel difficultyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name="recipeStatus")
    private RecipeStatus recipeStatus;

    @Column(name="duration")
    private String cookingDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chef_id")
    private User chef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryRecipe categoryRecipe;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "version")
    private Integer version;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType; // pdf, docx, jpg, png, txt

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;



    @PrePersist
    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    @PostUpdate
    protected void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }
}
