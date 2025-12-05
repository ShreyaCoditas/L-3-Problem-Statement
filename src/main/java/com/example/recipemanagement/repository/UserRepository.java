package com.example.recipemanagement.repository;

import com.example.recipemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByEmailIgnoreCase(String email);

   Optional<User> findByNameIgnoreCase(String name);
}
