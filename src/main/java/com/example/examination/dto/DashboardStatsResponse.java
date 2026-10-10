package com.example.examination.dto;

import java.util.List;

public class DashboardStatsResponse {
    private long totalStudents;
    private long totalExams;
    private long upcomingExams;
    private long activeExams;
    private long completedExams;
    private long totalQuestions;
    private long totalAttempts;
    private long totalResults;
    private long passCount;
    private long failCount;
    private double averagePercentage;
    private List<ExamResponse> recentExams;
    private List<ResultResponse> recentResults;

    public DashboardStatsResponse() {
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalExams() {
        return totalExams;
    }

    public void setTotalExams(long totalExams) {
        this.totalExams = totalExams;
    }

    public long getUpcomingExams() {
        return upcomingExams;
    }

    public void setUpcomingExams(long upcomingExams) {
        this.upcomingExams = upcomingExams;
    }

    public long getActiveExams() {
        return activeExams;
    }

    public void setActiveExams(long activeExams) {
        this.activeExams = activeExams;
    }

    public long getCompletedExams() {
        return completedExams;
    }

    public void setCompletedExams(long completedExams) {
        this.completedExams = completedExams;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
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

    public double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }

    public List<ExamResponse> getRecentExams() {
        return recentExams;
    }

    public void setRecentExams(List<ExamResponse> recentExams) {
        this.recentExams = recentExams;
    }

    public List<ResultResponse> getRecentResults() {
        return recentResults;
    }

    public void setRecentResults(List<ResultResponse> recentResults) {
        this.recentResults = recentResults;
    }
}
