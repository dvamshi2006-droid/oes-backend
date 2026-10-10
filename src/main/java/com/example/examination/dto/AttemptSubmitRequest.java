package com.example.examination.dto;

import java.util.ArrayList;
import java.util.List;

public class AttemptSubmitRequest {
    private List<AnswerSubmissionRequest> answers = new ArrayList<>();

    public AttemptSubmitRequest() {
    }

    public AttemptSubmitRequest(List<AnswerSubmissionRequest> answers) {
        this.answers = answers;
    }

    public List<AnswerSubmissionRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmissionRequest> answers) {
        this.answers = answers;
    }
}
