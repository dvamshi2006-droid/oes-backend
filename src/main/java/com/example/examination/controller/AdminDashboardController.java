package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.DashboardStatsResponse;
import com.example.examination.dto.ExamStatsResponse;
import com.example.examination.dto.UserDto;
import com.example.examination.service.DashboardService;
import com.example.examination.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboardStats() {
        DashboardStatsResponse stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard statistics retrieved", stats));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getAllExamStatistics() {
        List<ExamStatsResponse> stats = dashboardService.getAllExamStatistics();
        return ResponseEntity.ok(ApiResponse.success("Overall exam statistics retrieved", stats));
    }

    @GetMapping("/statistics/exams/{examId}")
    public ResponseEntity<ApiResponse> getExamStatistics(@PathVariable Long examId) {
        ExamStatsResponse stats = dashboardService.getExamStatistics(examId);
        return ResponseEntity.ok(ApiResponse.success("Exam statistics retrieved", stats));
    }

    @GetMapping("/statistics/students")
    public ResponseEntity<ApiResponse> getStudentStatistics() {
        List<UserDto> students = userService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Student records retrieved", students));
    }
}
