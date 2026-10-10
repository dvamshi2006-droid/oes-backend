package com.example.examination.dto;

import com.example.examination.entity.ResultStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ResultResponse {
    private Long id;
    private Long attemptId;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private String studentEmail;
    private Long examId;
    private String examTitle;
    private String subject;
    private LocalDate examDate;
    private Integer totalQuestions;
    private Integer attemptedQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer unansweredQuestions;
    private Integer totalMarks;
    private Integer obtainedMarks;
    private Double percentage;
    private ResultStatus resultStatus;
    private LocalDateTime createdAt;
    private List<AnswerReviewDto> reviewAnswers;

    public ResultResponse() {
    }

    public static class AnswerReviewDto {
        private Long questionId;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
        private Integer marks;
        private Integer marksAwarded;

        public AnswerReviewDto() {
        }

        public AnswerReviewDto(Long questionId, String questionText, String optionA, String optionB, String optionC, String optionD, String selectedAnswer, String correctAnswer, Boolean isCorrect, Integer marks, Integer marksAwarded) {
            this.questionId = questionId;
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
            this.marks = marks;
            this.marksAwarded = marksAwarded;
        }

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }

        public void setSelectedAnswer(String selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public Boolean getIsCorrect() {
            return isCorrect;
        }

        public void setIsCorrect(Boolean correct) {
            isCorrect = correct;
        }

        public Integer getMarks() {
            return marks;
        }

        public void setMarks(Integer marks) {
            this.marks = marks;
        }

        public Integer getMarksAwarded() {
            return marksAwarded;
        }

        public void setMarksAwarded(Integer marksAwarded) {
            this.marksAwarded = marksAwarded;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
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

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(Integer attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public Integer getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(Integer unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Integer obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<AnswerReviewDto> getReviewAnswers() {
        return reviewAnswers;
    }

    public void setReviewAnswers(List<AnswerReviewDto> reviewAnswers) {
        this.reviewAnswers = reviewAnswers;
    }
}
