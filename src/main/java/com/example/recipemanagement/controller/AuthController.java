package com.example.recipemanagement.controller;

import com.example.recipemanagement.dto.ApiResponseDto;
import com.example.recipemanagement.dto.CreateLoginDto;
import com.example.recipemanagement.dto.LoginResponseDto;
import com.example.recipemanagement.dto.CreateRegisterDto;
import com.example.recipemanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Void>> register(@Valid @RequestBody CreateRegisterDto registerDto){
        ApiResponseDto<Void> response=authService.register(registerDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@Valid @RequestBody CreateLoginDto createLoginDto){
        ApiResponseDto<LoginResponseDto> response=authService.login(createLoginDto);
        return ResponseEntity.ok(response);
    }
}
