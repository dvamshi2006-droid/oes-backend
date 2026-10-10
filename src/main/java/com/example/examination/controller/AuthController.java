package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.AuthRequest;
import com.example.examination.dto.AuthResponse;
import com.example.examination.dto.RegisterRequest;
import com.example.examination.dto.UserDto;
import com.example.examination.security.UserPrincipal;
import com.example.examination.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return new ResponseEntity<>(ApiResponse.success("Registration successful", response), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return new ResponseEntity<>(ApiResponse.error("User is not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        UserDto userDto = authService.getCurrentUser(currentUser);
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved", userDto));
    }
}
