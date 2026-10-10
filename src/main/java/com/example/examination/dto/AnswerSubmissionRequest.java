package com.example.examination.dto;

public class AnswerSubmissionRequest {
    private Long questionId;
    private String selectedAnswer; // "A", "B", "C", "D" or null

    public AnswerSubmissionRequest() {
    }

    public AnswerSubmissionRequest(Long questionId, String selectedAnswer) {
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
