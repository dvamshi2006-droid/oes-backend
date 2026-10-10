package com.example.examination.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false, unique = true)
    private ExamAttempt attempt;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions = 0;

    @Column(name = "attempted_questions", nullable = false)
    private Integer attemptedQuestions = 0;

    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers = 0;

    @Column(name = "incorrect_answers", nullable = false)
    private Integer incorrectAnswers = 0;

    @Column(name = "unanswered_questions", nullable = false)
    private Integer unansweredQuestions = 0;

    @Column(name = "total_marks", nullable = false)
    private Integer totalMarks = 0;

    @Column(name = "obtained_marks", nullable = false)
    private Integer obtainedMarks = 0;

    @Column(nullable = false)
    private Double percentage = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false, length = 10)
    private ResultStatus resultStatus = ResultStatus.FAIL;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Result() {
    }

    public Result(User student, Exam exam, ExamAttempt attempt, Integer totalQuestions, Integer attemptedQuestions,
                  Integer correctAnswers, Integer incorrectAnswers, Integer unansweredQuestions,
                  Integer totalMarks, Integer obtainedMarks, Double percentage, ResultStatus resultStatus) {
        this.student = student;
        this.exam = exam;
        this.attempt = attempt;
        this.totalQuestions = totalQuestions;
        this.attemptedQuestions = attemptedQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.unansweredQuestions = unansweredQuestions;
        this.totalMarks = totalMarks;
        this.obtainedMarks = obtainedMarks;
        this.percentage = percentage;
        this.resultStatus = resultStatus;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(ExamAttempt attempt) {
        this.attempt = attempt;
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
}
