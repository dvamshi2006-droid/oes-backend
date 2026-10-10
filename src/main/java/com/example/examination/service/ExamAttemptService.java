package com.example.examination.service;

import com.example.examination.dto.*;
import com.example.examination.entity.*;
import com.example.examination.exception.BadRequestException;
import com.example.examination.exception.ResourceNotFoundException;
import com.example.examination.exception.UnauthorizedException;
import com.example.examination.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamAttemptService {

    @Autowired
    private ExamAttemptRepository attemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @Transactional
    public AttemptStartResponse startExam(Long examId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "id", examId));

        if (exam.getStatus() != ExamStatus.ACTIVE) {
            throw new BadRequestException("This examination is currently not active for attempts.");
        }

        // Check if student already submitted this exam
        Optional<Result> existingResult = resultRepository.findByStudentIdAndExamId(studentId, examId);
        if (existingResult.isPresent()) {
            throw new BadRequestException("You have already completed this examination. Check your result history.");
        }

        // Check for existing IN_PROGRESS attempt
        Optional<ExamAttempt> existingAttempt = attemptRepository.findByStudentIdAndExamIdAndStatus(
                studentId, examId, AttemptStatus.IN_PROGRESS
        );

        ExamAttempt attempt;
        LocalDateTime now = LocalDateTime.now();

        if (existingAttempt.isPresent()) {
            attempt = existingAttempt.get();
            // Check if deadline has already passed
            if (now.isAfter(attempt.getEndTime())) {
                // Auto-submit expired attempt
                evaluateAndFinalizeAttempt(attempt, new ArrayList<>());
                throw new BadRequestException("The time allotted for this examination has already expired.");
            }
        } else {
            // Create new attempt
            LocalDateTime endTime = now.plusMinutes(exam.getDuration());
            attempt = new ExamAttempt(student, exam, now, endTime);
            attempt = attemptRepository.save(attempt);
        }

        long remainingSeconds = Math.max(0, Duration.between(now, attempt.getEndTime()).getSeconds());
        List<QuestionStudentResponse> questions = questionService.getQuestionsForStudent(examId);

        return new AttemptStartResponse(
                attempt.getId(),
                exam.getId(),
                exam.getTitle(),
                exam.getSubject(),
                exam.getDuration(),
                exam.getTotalMarks(),
                exam.getPassingMarks(),
                attempt.getStartTime(),
                attempt.getEndTime(),
                remainingSeconds,
                questions
        );
    }

    public ExamAttempt getAttemptById(Long attemptId, Long studentId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam Attempt", "id", attemptId));

        if (!attempt.getStudent().getId().equals(studentId)) {
            throw new UnauthorizedException("You are not authorized to view this examination attempt.");
        }

        return attempt;
    }

    @Transactional
    public void saveAnswer(Long attemptId, Long studentId, Long questionId, String selectedAnswer) {
        ExamAttempt attempt = getAttemptById(attemptId, studentId);

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new BadRequestException("This exam attempt is already finalized.");
        }

        if (LocalDateTime.now().isAfter(attempt.getEndTime())) {
            // Automatically finalize expired attempt
            evaluateAndFinalizeAttempt(attempt, new ArrayList<>());
            throw new BadRequestException("Examination time has expired. Your attempt has been automatically submitted.");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));

        if (!question.getExam().getId().equals(attempt.getExam().getId())) {
            throw new BadRequestException("Question does not belong to this examination.");
        }

        Optional<Answer> existingAnswer = answerRepository.findByAttemptIdAndQuestionId(attemptId, questionId);
        Answer answer;
        if (existingAnswer.isPresent()) {
            answer = existingAnswer.get();
            answer.setSelectedAnswer(selectedAnswer != null ? selectedAnswer.toUpperCase() : null);
        } else {
            answer = new Answer(attempt, question, selectedAnswer != null ? selectedAnswer.toUpperCase() : null);
        }
        answerRepository.save(answer);
    }

    @Transactional
    public ResultResponse submitExam(Long attemptId, Long studentId, AttemptSubmitRequest request) {
        ExamAttempt attempt = getAttemptById(attemptId, studentId);

        // Prevent duplicate submissions: return existing result if already submitted
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            return resultRepository.findByAttemptId(attemptId)
                    .map(this::mapToResultResponse)
                    .orElseThrow(() -> new BadRequestException("Exam attempt is already submitted."));
        }

        List<AnswerSubmissionRequest> submittedAnswers = (request != null && request.getAnswers() != null)
                ? request.getAnswers()
                : new ArrayList<>();

        return evaluateAndFinalizeAttempt(attempt, submittedAnswers);
    }

    @Transactional
    public ResultResponse evaluateAndFinalizeAttempt(ExamAttempt attempt, List<AnswerSubmissionRequest> submittedAnswers) {
        Exam exam = attempt.getExam();
        User student = attempt.getStudent();
        List<Question> allQuestions = questionRepository.findByExamId(exam.getId());

        // Map submitted answers by question ID
        Map<Long, String> answerMap = new HashMap<>();
        for (AnswerSubmissionRequest req : submittedAnswers) {
            if (req.getQuestionId() != null && req.getSelectedAnswer() != null) {
                answerMap.put(req.getQuestionId(), req.getSelectedAnswer().toUpperCase().trim());
            }
        }

        // Also fetch any previously incrementally saved answers
        List<Answer> existingSavedAnswers = answerRepository.findByAttemptId(attempt.getId());
        for (Answer saved : existingSavedAnswers) {
            if (!answerMap.containsKey(saved.getQuestion().getId()) && saved.getSelectedAnswer() != null) {
                answerMap.put(saved.getQuestion().getId(), saved.getSelectedAnswer());
            }
        }

        int totalQuestions = allQuestions.size();
        int attemptedQuestions = 0;
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        int unansweredQuestions = 0;
        int obtainedMarks = 0;
        int examTotalMarks = exam.getTotalMarks();

        List<ResultResponse.AnswerReviewDto> reviewList = new ArrayList<>();

        for (Question question : allQuestions) {
            String selected = answerMap.get(question.getId());
            boolean isAttempted = selected != null && !selected.trim().isEmpty();
            boolean isCorrect = false;
            int marksAwarded = 0;

            if (isAttempted) {
                attemptedQuestions++;
                if (selected.equalsIgnoreCase(question.getCorrectAnswer())) {
                    isCorrect = true;
                    correctAnswers++;
                    marksAwarded = question.getMarks();
                    obtainedMarks += marksAwarded;
                } else {
                    incorrectAnswers++;
                }
            } else {
                unansweredQuestions++;
            }

            // Save or update answer entity
            Optional<Answer> existing = answerRepository.findByAttemptIdAndQuestionId(attempt.getId(), question.getId());
            Answer answerEntity;
            if (existing.isPresent()) {
                answerEntity = existing.get();
                answerEntity.setSelectedAnswer(selected);
                answerEntity.setIsCorrect(isCorrect);
                answerEntity.setMarksAwarded(marksAwarded);
            } else {
                answerEntity = new Answer(attempt, question, selected);
                answerEntity.setIsCorrect(isCorrect);
                answerEntity.setMarksAwarded(marksAwarded);
            }
            answerRepository.save(answerEntity);

            reviewList.add(new ResultResponse.AnswerReviewDto(
                    question.getId(),
                    question.getQuestionText(),
                    question.getOptionA(),
                    question.getOptionB(),
                    question.getOptionC(),
                    question.getOptionD(),
                    selected,
                    question.getCorrectAnswer(),
                    isCorrect,
                    question.getMarks(),
                    marksAwarded
            ));
        }

        double percentage = (examTotalMarks > 0) ? ((double) obtainedMarks / examTotalMarks) * 100.0 : 0.0;
        percentage = Math.round(percentage * 100.0) / 100.0; // 2 decimal places

        ResultStatus resultStatus = (obtainedMarks >= exam.getPassingMarks()) ? ResultStatus.PASS : ResultStatus.FAIL;

        // Finalize Attempt
        attempt.setStatus(AttemptStatus.SUBMITTED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attemptRepository.save(attempt);

        // Save Result
        Result result = new Result(
                student,
                exam,
                attempt,
                totalQuestions,
                attemptedQuestions,
                correctAnswers,
                incorrectAnswers,
                unansweredQuestions,
                examTotalMarks,
                obtainedMarks,
                percentage,
                resultStatus
        );

        Result savedResult = resultRepository.save(result);

        ResultResponse response = mapToResultResponse(savedResult);
        response.setReviewAnswers(reviewList);
        return response;
    }

    public ResultResponse mapToResultResponse(Result result) {
        ResultResponse response = new ResultResponse();
        response.setId(result.getId());
        response.setAttemptId(result.getAttempt().getId());
        response.setStudentId(result.getStudent().getId());
        response.setStudentName(result.getStudent().getName());
        response.setStudentCode(result.getStudent().getStudentId());
        response.setStudentEmail(result.getStudent().getEmail());
        response.setExamId(result.getExam().getId());
        response.setExamTitle(result.getExam().getTitle());
        response.setSubject(result.getExam().getSubject());
        response.setExamDate(result.getExam().getExamDate());
        response.setTotalQuestions(result.getTotalQuestions());
        response.setAttemptedQuestions(result.getAttemptedQuestions());
        response.setCorrectAnswers(result.getCorrectAnswers());
        response.setIncorrectAnswers(result.getIncorrectAnswers());
        response.setUnansweredQuestions(result.getUnansweredQuestions());
        response.setTotalMarks(result.getTotalMarks());
        response.setObtainedMarks(result.getObtainedMarks());
        response.setPercentage(result.getPercentage());
        response.setResultStatus(result.getResultStatus());
        response.setCreatedAt(result.getCreatedAt());
        return response;
    }
}
