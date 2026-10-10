package com.example.examination.controller;

import com.example.examination.dto.*;
import com.example.examination.security.UserPrincipal;
import com.example.examination.service.ExamAttemptService;
import com.example.examination.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExamAttemptController {

    @Autowired
    private ExamAttemptService attemptService;

    @Autowired
    private QuestionService questionService;

    @PostMapping("/api/exams/{examId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> startExam(@PathVariable Long examId,
                                                 @AuthenticationPrincipal UserPrincipal currentUser) {
        AttemptStartResponse response = attemptService.startExam(examId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Exam started successfully", response));
    }

    @GetMapping("/api/attempts/{attemptId}/questions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> getAttemptQuestions(@PathVariable Long attemptId,
                                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        var attempt = attemptService.getAttemptById(attemptId, currentUser.getId());
        List<QuestionStudentResponse> questions = questionService.getQuestionsForStudent(attempt.getExam().getId());
        return ResponseEntity.ok(ApiResponse.success("Attempt questions retrieved", questions));
    }

    @PutMapping("/api/attempts/{attemptId}/answers/{questionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> saveAnswer(@PathVariable Long attemptId,
                                                  @PathVariable Long questionId,
                                                  @RequestBody AnswerSubmissionRequest answerRequest,
                                                  @AuthenticationPrincipal UserPrincipal currentUser) {
        attemptService.saveAnswer(attemptId, currentUser.getId(), questionId, answerRequest.getSelectedAnswer());
        return ResponseEntity.ok(ApiResponse.success("Answer saved successfully"));
    }

    @PostMapping("/api/attempts/{attemptId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> submitExam(@PathVariable Long attemptId,
                                                  @RequestBody(required = false) AttemptSubmitRequest submitRequest,
                                                  @AuthenticationPrincipal UserPrincipal currentUser) {
        ResultResponse result = attemptService.submitExam(attemptId, currentUser.getId(), submitRequest);
        return ResponseEntity.ok(ApiResponse.success("Exam submitted and evaluated successfully", result));
    }
}
