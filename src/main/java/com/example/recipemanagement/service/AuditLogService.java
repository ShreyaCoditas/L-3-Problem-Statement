package com.example.recipemanagement.service;

import com.example.recipemanagement.entity.AuditLog;
import com.example.recipemanagement.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(Long userId, Long recipeId, String action) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setRecipeId(recipeId);
        log.setAction(action);
        auditLogRepository.save(log);
    }
}

