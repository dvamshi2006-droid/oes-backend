package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.ResultResponse;
import com.example.examination.entity.Role;
import com.example.examination.security.UserPrincipal;
import com.example.examination.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllResults() {
        List<ResultResponse> results = resultService.getAllResults();
        return ResponseEntity.ok(ApiResponse.success("All results retrieved successfully", results));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyResults(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<ResultResponse> results = resultService.getResultsByStudent(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Your results retrieved successfully", results));
    }

    @GetMapping("/me/history")
    public ResponseEntity<ApiResponse> getMyHistory(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<ResultResponse> results = resultService.getResultsByStudent(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Your exam history retrieved successfully", results));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getResultsByStudent(@PathVariable Long studentId) {
        List<ResultResponse> results = resultService.getResultsByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student results retrieved successfully", results));
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getResultsByExam(@PathVariable Long examId) {
        List<ResultResponse> results = resultService.getResultsByExam(examId);
        return ResponseEntity.ok(ApiResponse.success("Exam results retrieved successfully", results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getResultById(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserPrincipal currentUser) {
        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        ResultResponse result = resultService.getResultById(id, currentUser.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Result details retrieved successfully", result));
    }
}
