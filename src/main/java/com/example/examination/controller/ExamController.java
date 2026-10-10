package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.ExamRequest;
import com.example.examination.dto.ExamResponse;
import com.example.examination.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllExams() {
        List<ExamResponse> exams = examService.getAllExams();
        return ResponseEntity.ok(ApiResponse.success("Exams retrieved successfully", exams));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableExams() {
        List<ExamResponse> exams = examService.getAvailableExams();
        return ResponseEntity.ok(ApiResponse.success("Available exams retrieved successfully", exams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getExamById(@PathVariable Long id) {
        ExamResponse exam = examService.getExamById(id);
        return ResponseEntity.ok(ApiResponse.success("Exam details retrieved successfully", exam));
    }

    @GetMapping("/{id}/instructions")
    public ResponseEntity<ApiResponse> getExamInstructions(@PathVariable Long id) {
        ExamResponse exam = examService.getExamById(id);
        return ResponseEntity.ok(ApiResponse.success("Exam instructions retrieved successfully", exam));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createExam(@Valid @RequestBody ExamRequest examRequest) {
        ExamResponse createdExam = examService.createExam(examRequest);
        return new ResponseEntity<>(ApiResponse.success("Exam created successfully", createdExam), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateExam(@PathVariable Long id, @Valid @RequestBody ExamRequest examRequest) {
        ExamResponse updatedExam = examService.updateExam(id, examRequest);
        return ResponseEntity.ok(ApiResponse.success("Exam updated successfully", updatedExam));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok(ApiResponse.success("Exam deleted successfully"));
    }
}
