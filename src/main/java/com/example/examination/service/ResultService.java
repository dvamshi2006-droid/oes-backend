package com.example.examination.service;

import com.example.examination.dto.ResultResponse;
import com.example.examination.entity.Answer;
import com.example.examination.entity.Question;
import com.example.examination.entity.Result;
import com.example.examination.exception.ResourceNotFoundException;
import com.example.examination.exception.UnauthorizedException;
import com.example.examination.repository.AnswerRepository;
import com.example.examination.repository.QuestionRepository;
import com.example.examination.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamAttemptService attemptService;

    public List<ResultResponse> getAllResults() {
        return resultRepository.findAll().stream()
                .map(attemptService::mapToResultResponse)
                .collect(Collectors.toList());
    }

    public List<ResultResponse> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(attemptService::mapToResultResponse)
                .collect(Collectors.toList());
    }

    public List<ResultResponse> getResultsByExam(Long examId) {
        return resultRepository.findByExamIdOrderByCreatedAtDesc(examId).stream()
                .map(attemptService::mapToResultResponse)
                .collect(Collectors.toList());
    }

    public ResultResponse getResultById(Long id, Long currentUserId, boolean isAdmin) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result", "id", id));

        if (!isAdmin && !result.getStudent().getId().equals(currentUserId)) {
            throw new UnauthorizedException("You are not authorized to view this result.");
        }

        ResultResponse response = attemptService.mapToResultResponse(result);

        // Include review answers with questions
        List<Answer> answers = answerRepository.findByAttemptId(result.getAttempt().getId());
        Map<Long, Answer> answerMap = answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a, (k1, k2) -> k1));

        List<Question> questions = questionRepository.findByExamId(result.getExam().getId());
        List<ResultResponse.AnswerReviewDto> reviewList = new ArrayList<>();

        for (Question q : questions) {
            Answer a = answerMap.get(q.getId());
            String selected = (a != null) ? a.getSelectedAnswer() : null;
            boolean isCorrect = (a != null && a.getIsCorrect() != null) ? a.getIsCorrect() : false;
            int marksAwarded = (a != null && a.getMarksAwarded() != null) ? a.getMarksAwarded() : 0;

            reviewList.add(new ResultResponse.AnswerReviewDto(
                    q.getId(),
                    q.getQuestionText(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getOptionD(),
                    selected,
                    q.getCorrectAnswer(),
                    isCorrect,
                    q.getMarks(),
                    marksAwarded
            ));
        }

        response.setReviewAnswers(reviewList);
        return response;
    }
}
