package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.UserDto;
import com.example.examination.security.UserPrincipal;
import com.example.examination.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<UserDto> students = userService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        UserDto student = userService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updated = userService.updateStudent(id, userDto);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        userService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserDto student = userService.getStudentById(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", student));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMyProfile(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody UserDto userDto) {
        UserDto updated = userService.updateStudent(currentUser.getId(), userDto);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }
}
