package com.example.examination.repository;

import com.example.examination.entity.Exam;
import com.example.examination.entity.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByStatus(ExamStatus status);
    List<Exam> findBySubjectContainingIgnoreCase(String subject);
    long countByStatus(ExamStatus status);
}
