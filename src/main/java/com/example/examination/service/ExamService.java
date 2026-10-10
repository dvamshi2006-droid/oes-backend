package com.example.examination.service;

import com.example.examination.dto.ExamRequest;
import com.example.examination.dto.ExamResponse;
import com.example.examination.entity.Exam;
import com.example.examination.entity.ExamStatus;
import com.example.examination.exception.BadRequestException;
import com.example.examination.exception.ResourceNotFoundException;
import com.example.examination.repository.ExamRepository;
import com.example.examination.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ExamResponse> getAvailableExams() {
        return examRepository.findByStatus(ExamStatus.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));
        return mapToResponse(exam);
    }

    public Exam getExamEntityById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));
    }

    @Transactional
    public ExamResponse createExam(ExamRequest request) {
        validateExamRequest(request);

        Exam exam = new Exam(
                request.getTitle(),
                request.getDescription(),
                request.getSubject(),
                request.getExamDate(),
                request.getStartTime(),
                request.getDuration(),
                request.getTotalMarks(),
                request.getPassingMarks(),
                request.getStatus() != null ? request.getStatus() : ExamStatus.UPCOMING
        );

        Exam savedExam = examRepository.save(exam);
        return mapToResponse(savedExam);
    }

    @Transactional
    public ExamResponse updateExam(Long id, ExamRequest request) {
        validateExamRequest(request);

        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));

        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setSubject(request.getSubject());
        exam.setExamDate(request.getExamDate());
        exam.setStartTime(request.getStartTime());
        exam.setDuration(request.getDuration());
        exam.setTotalMarks(request.getTotalMarks());
        exam.setPassingMarks(request.getPassingMarks());
        if (request.getStatus() != null) {
            exam.setStatus(request.getStatus());
        }

        Exam updatedExam = examRepository.save(exam);
        return mapToResponse(updatedExam);
    }

    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", id));
        examRepository.delete(exam);
    }

    private void validateExamRequest(ExamRequest request) {
        if (request.getPassingMarks() > request.getTotalMarks()) {
            throw new BadRequestException("Passing marks cannot exceed total marks");
        }
        if (request.getDuration() <= 0) {
            throw new BadRequestException("Exam duration must be greater than 0 minutes");
        }
    }

    public ExamResponse mapToResponse(Exam exam) {
        int questionCount = (int) questionRepository.countByExamId(exam.getId());
        return new ExamResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getSubject(),
                exam.getExamDate(),
                exam.getStartTime(),
                exam.getDuration(),
                exam.getTotalMarks(),
                exam.getPassingMarks(),
                exam.getStatus(),
                questionCount,
                exam.getCreatedAt()
        );
    }
}
