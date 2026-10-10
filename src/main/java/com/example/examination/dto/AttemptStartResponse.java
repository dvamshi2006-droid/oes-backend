package com.example.examination.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AttemptStartResponse {
    private Long attemptId;
    private Long examId;
    private String examTitle;
    private String subject;
    private Integer durationMinutes;
    private Integer totalMarks;
    private Integer passingMarks;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long remainingSeconds;
    private List<QuestionStudentResponse> questions;

    public AttemptStartResponse() {
    }

    public AttemptStartResponse(Long attemptId, Long examId, String examTitle, String subject, Integer durationMinutes, Integer totalMarks, Integer passingMarks, LocalDateTime startTime, LocalDateTime endTime, Long remainingSeconds, List<QuestionStudentResponse> questions) {
        this.attemptId = attemptId;
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.passingMarks = passingMarks;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remainingSeconds = remainingSeconds;
        this.questions = questions;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getPassingMarks() {
        return passingMarks;
    }

    public void setPassingMarks(Integer passingMarks) {
        this.passingMarks = passingMarks;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(Long remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public List<QuestionStudentResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionStudentResponse> questions) {
        this.questions = questions;
    }
}
