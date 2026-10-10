package com.example.examination.service;

import com.example.examination.dto.DashboardStatsResponse;
import com.example.examination.dto.ExamResponse;
import com.example.examination.dto.ExamStatsResponse;
import com.example.examination.dto.ResultResponse;
import com.example.examination.entity.Exam;
import com.example.examination.entity.ExamStatus;
import com.example.examination.entity.Result;
import com.example.examination.entity.ResultStatus;
import com.example.examination.entity.Role;
import com.example.examination.exception.ResourceNotFoundException;
import com.example.examination.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamAttemptRepository attemptRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamAttemptService attemptService;

    public DashboardStatsResponse getAdminDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        stats.setTotalStudents(userRepository.countByRole(Role.ROLE_STUDENT));
        stats.setTotalExams(examRepository.count());
        stats.setUpcomingExams(examRepository.countByStatus(ExamStatus.UPCOMING));
        stats.setActiveExams(examRepository.countByStatus(ExamStatus.ACTIVE));
        stats.setCompletedExams(examRepository.countByStatus(ExamStatus.COMPLETED));
        stats.setTotalQuestions(questionRepository.count());
        stats.setTotalAttempts(attemptRepository.count());
        stats.setTotalResults(resultRepository.count());
        stats.setPassCount(resultRepository.countByResultStatus(ResultStatus.PASS));
        stats.setFailCount(resultRepository.countByResultStatus(ResultStatus.FAIL));

        Double avg = resultRepository.getAveragePercentage();
        stats.setAveragePercentage(avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0);

        List<ExamResponse> recentExams = examRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).stream().map(examService::mapToResponse).collect(Collectors.toList());
        stats.setRecentExams(recentExams);

        List<ResultResponse> recentResults = resultRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).stream().map(attemptService::mapToResultResponse).collect(Collectors.toList());
        stats.setRecentResults(recentResults);

        return stats;
    }

    public List<ExamStatsResponse> getAllExamStatistics() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream().map(this::calculateExamStats).collect(Collectors.toList());
    }

    public ExamStatsResponse getExamStatistics(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", examId));
        return calculateExamStats(exam);
    }

    private ExamStatsResponse calculateExamStats(Exam exam) {
        List<Result> results = resultRepository.findByExamIdOrderByCreatedAtDesc(exam.getId());
        long totalAttempts = results.size();
        long passCount = results.stream().filter(r -> r.getResultStatus() == ResultStatus.PASS).count();
        long failCount = results.stream().filter(r -> r.getResultStatus() == ResultStatus.FAIL).count();

        double passPercentage = totalAttempts > 0 ? ((double) passCount / totalAttempts) * 100.0 : 0.0;
        passPercentage = Math.round(passPercentage * 100.0) / 100.0;

        double avgMarks = results.stream().mapToInt(Result::getObtainedMarks).average().orElse(0.0);
        avgMarks = Math.round(avgMarks * 100.0) / 100.0;

        double avgPercent = results.stream().mapToDouble(Result::getPercentage).average().orElse(0.0);
        avgPercent = Math.round(avgPercent * 100.0) / 100.0;

        return new ExamStatsResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getSubject(),
                totalAttempts,
                passCount,
                failCount,
                passPercentage,
                avgMarks,
                avgPercent
        );
    }
}
