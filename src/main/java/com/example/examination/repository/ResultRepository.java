package com.example.examination.repository;

import com.example.examination.entity.Result;
import com.example.examination.entity.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Result> findByExamIdOrderByCreatedAtDesc(Long examId);
    Optional<Result> findByAttemptId(Long attemptId);
    Optional<Result> findByStudentIdAndExamId(Long studentId, Long examId);
    long countByResultStatus(ResultStatus status);

    @Query("SELECT AVG(r.percentage) FROM Result r")
    Double getAveragePercentage();

    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.exam.id = :examId")
    Double getAveragePercentageByExamId(Long examId);
}
