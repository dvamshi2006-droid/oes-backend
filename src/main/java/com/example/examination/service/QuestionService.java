package com.example.examination.service;

import com.example.examination.dto.QuestionAdminResponse;
import com.example.examination.dto.QuestionRequest;
import com.example.examination.dto.QuestionStudentResponse;
import com.example.examination.entity.Exam;
import com.example.examination.entity.Question;
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
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    public List<QuestionAdminResponse> getQuestionsForAdmin(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam", "id", examId);
        }
        return questionRepository.findByExamId(examId).stream()
                .map(this::mapToAdminResponse)
                .collect(Collectors.toList());
    }

    public List<QuestionStudentResponse> getQuestionsForStudent(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam", "id", examId);
        }
        return questionRepository.findByExamId(examId).stream()
                .map(this::mapToStudentResponse)
                .collect(Collectors.toList());
    }

    public QuestionAdminResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        return mapToAdminResponse(question);
    }

    @Transactional
    public QuestionAdminResponse addQuestion(Long examId, QuestionRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", examId));

        validateCorrectAnswer(request.getCorrectAnswer());

        Question question = new Question(
                exam,
                request.getQuestionText(),
                request.getOptionA(),
                request.getOptionB(),
                request.getOptionC(),
                request.getOptionD(),
                request.getCorrectAnswer().toUpperCase(),
                request.getMarks() != null ? request.getMarks() : 1
        );

        Question savedQuestion = questionRepository.save(question);
        return mapToAdminResponse(savedQuestion);
    }

    @Transactional
    public QuestionAdminResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        if (request.getCorrectAnswer() != null) {
            validateCorrectAnswer(request.getCorrectAnswer());
            question.setCorrectAnswer(request.getCorrectAnswer().toUpperCase());
        }

        if (request.getQuestionText() != null) question.setQuestionText(request.getQuestionText());
        if (request.getOptionA() != null) question.setOptionA(request.getOptionA());
        if (request.getOptionB() != null) question.setOptionB(request.getOptionB());
        if (request.getOptionC() != null) question.setOptionC(request.getOptionC());
        if (request.getOptionD() != null) question.setOptionD(request.getOptionD());
        if (request.getMarks() != null) question.setMarks(request.getMarks());

        Question updatedQuestion = questionRepository.save(question);
        return mapToAdminResponse(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        questionRepository.delete(question);
    }

    private void validateCorrectAnswer(String correctAnswer) {
        if (correctAnswer == null || !correctAnswer.matches("^[A-Da-d]$")) {
            throw new BadRequestException("Correct answer must be A, B, C, or D");
        }
    }

    public QuestionAdminResponse mapToAdminResponse(Question q) {
        return new QuestionAdminResponse(
                q.getId(),
                q.getExam().getId(),
                q.getQuestionText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getCorrectAnswer(),
                q.getMarks(),
                q.getCreatedAt()
        );
    }

    public QuestionStudentResponse mapToStudentResponse(Question q) {
        return new QuestionStudentResponse(
                q.getId(),
                q.getExam().getId(),
                q.getQuestionText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getMarks()
        );
    }
}
