package com.example.examination.repository;

import com.example.examination.entity.AttemptStatus;
import com.example.examination.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    List<ExamAttempt> findByStudentId(Long studentId);
    List<ExamAttempt> findByExamId(Long examId);
    Optional<ExamAttempt> findByStudentIdAndExamIdAndStatus(Long studentId, Long examId, AttemptStatus status);
    List<ExamAttempt> findByStudentIdAndExamId(Long studentId, Long examId);
    long countByStatus(AttemptStatus status);
}
