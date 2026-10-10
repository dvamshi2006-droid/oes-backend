package com.example.examination.dto;

public class ExamStatsResponse {
    private Long examId;
    private String examTitle;
    private String subject;
    private long totalAttempts;
    private long passCount;
    private long failCount;
    private double passPercentage;
    private double averageMarks;
    private double averagePercentage;

    public ExamStatsResponse() {
    }

    public ExamStatsResponse(Long examId, String examTitle, String subject, long totalAttempts, long passCount, long failCount, double passPercentage, double averageMarks, double averagePercentage) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.totalAttempts = totalAttempts;
        this.passCount = passCount;
        this.failCount = failCount;
        this.passPercentage = passPercentage;
        this.averageMarks = averageMarks;
        this.averagePercentage = averagePercentage;
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

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public long getPassCount() {
        return passCount;
    }

    public void setPassCount(long passCount) {
        this.passCount = passCount;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public double getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(double passPercentage) {
        this.passPercentage = passPercentage;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }
}
