package com.example.recipemanagement.controller;

import com.example.recipemanagement.entity.AuditLog;
import com.example.recipemanagement.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/audit")
public class AuditController {

    @Autowired
    private  AuditLogRepository auditLogRepository;

    @GetMapping("/all")
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
