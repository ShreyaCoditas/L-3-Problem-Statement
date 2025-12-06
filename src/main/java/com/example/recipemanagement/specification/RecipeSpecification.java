package com.example.recipemanagement.specification;

import com.example.recipemanagement.constants.Cuisine;
import com.example.recipemanagement.constants.DifficultyLevel;
import com.example.recipemanagement.entity.Recipe;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {

    public static Specification<Recipe> search(
            String title,
            String ingredient,
            String category,
            Cuisine cuisine,
            DifficultyLevel difficultyLevel
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"));
            }

            if (ingredient != null && !ingredient.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("ingredients")),
                        "%" + ingredient.toLowerCase() + "%"));
            }

            if (category != null && !category.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("categoryRecipe").get("name")),
                        "%" + category.toLowerCase() + "%"));
            }

            if (cuisine != null) {
                predicates.add(cb.equal(root.get("categoryRecipe").get("cuisine"), cuisine));
            }

            if (difficultyLevel != null) {
                predicates.add(cb.equal(root.get("difficultyLevel"), difficultyLevel));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
