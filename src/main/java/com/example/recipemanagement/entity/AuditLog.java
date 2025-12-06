package com.example.recipemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String action;     // CREATE_RECIPE, UPDATE_RECIPE, DELETE_RECIPE, VIEW_RECIPE

    private Long recipeId;

    private LocalDateTime timestamp;

    @PrePersist
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}

