package com.example.recipemanagement.entity;


import com.example.recipemanagement.constants.Cuisine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="category_recipe")
public class CategoryRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="cuisine")
    private Cuisine cuisine;

    @Column(name="created_At")
    private LocalDateTime createdAt;

    @Column(name="updated_At")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "categoryRecipe")
    List<Recipe> recipes=new ArrayList<>();

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
