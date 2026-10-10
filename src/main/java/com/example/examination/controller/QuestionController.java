package com.example.examination.controller;

import com.example.examination.dto.ApiResponse;
import com.example.examination.dto.QuestionAdminResponse;
import com.example.examination.dto.QuestionRequest;
import com.example.examination.entity.Role;
import com.example.examination.security.UserPrincipal;
import com.example.examination.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/api/exams/{examId}/questions")
    public ResponseEntity<ApiResponse> getQuestionsByExam(@PathVariable Long examId,
                                                          @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser != null && currentUser.getRole() == Role.ROLE_ADMIN) {
            return ResponseEntity.ok(ApiResponse.success(
                    "Questions retrieved successfully",
                    questionService.getQuestionsForAdmin(examId)
            ));
        } else {
            return ResponseEntity.ok(ApiResponse.success(
                    "Questions retrieved successfully",
                    questionService.getQuestionsForStudent(examId)
            ));
        }
    }

    @PostMapping("/api/exams/{examId}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addQuestion(@PathVariable Long examId,
                                                   @Valid @RequestBody QuestionRequest questionRequest) {
        QuestionAdminResponse created = questionService.addQuestion(examId, questionRequest);
        return new ResponseEntity<>(ApiResponse.success("Question added successfully", created), HttpStatus.CREATED);
    }

    @GetMapping("/api/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getQuestionById(@PathVariable Long id) {
        QuestionAdminResponse question = questionService.getQuestionById(id);
        return ResponseEntity.ok(ApiResponse.success("Question retrieved successfully", question));
    }

    @PutMapping("/api/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateQuestion(@PathVariable Long id,
                                                      @Valid @RequestBody QuestionRequest questionRequest) {
        QuestionAdminResponse updated = questionService.updateQuestion(id, questionRequest);
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", updated));
    }

    @DeleteMapping("/api/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully"));
    }
}
