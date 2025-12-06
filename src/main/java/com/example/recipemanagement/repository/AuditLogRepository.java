package com.example.recipemanagement.repository;

import com.example.recipemanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

