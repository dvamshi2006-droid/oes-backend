package com.example.examination.config;

import com.example.examination.entity.*;
import com.example.examination.repository.ExamRepository;
import com.example.examination.repository.QuestionRepository;
import com.example.examination.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeExamsAndQuestions();
    }

    private void initializeUsers() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User(
                    "System Administrator",
                    null,
                    "admin@example.com",
                    "1234567890",
                    passwordEncoder.encode("admin123"),
                    Role.ROLE_ADMIN
            );
            userRepository.save(admin);
            logger.info("Created default Admin account: admin@example.com / admin123");
        }

        if (!userRepository.existsByEmail("student@example.com")) {
            User student = new User(
                    "John Doe",
                    "STU001",
                    "student@example.com",
                    "9876543210",
                    passwordEncoder.encode("student123"),
                    Role.ROLE_STUDENT
            );
            userRepository.save(student);
            logger.info("Created default Student account: student@example.com / student123");
        }

        if (!userRepository.existsByEmail("jane.smith@example.com")) {
            User student2 = new User(
                    "Jane Smith",
                    "STU002",
                    "jane.smith@example.com",
                    "9876543211",
                    passwordEncoder.encode("student123"),
                    Role.ROLE_STUDENT
            );
            userRepository.save(student2);
        }
    }

    private void initializeExamsAndQuestions() {
        if (examRepository.count() == 0) {
            // Exam 1: Java Programming Certification
            Exam javaExam = new Exam(
                    "Java Fundamentals Certification",
                    "Covers Core Java, OOPs concepts, Collections, Exception Handling and JVM architecture.",
                    "Java Programming",
                    LocalDate.now(),
                    LocalTime.of(9, 0),
                    45, // 45 minutes
                    50, // total marks
                    20, // passing marks
                    ExamStatus.ACTIVE
            );
            javaExam = examRepository.save(javaExam);

            questionRepository.save(new Question(
                    javaExam,
                    "Which keyword is used to inherit a class in Java?",
                    "implements",
                    "extends",
                    "inherits",
                    "instanceof",
                    "B",
                    10
            ));

            questionRepository.save(new Question(
                    javaExam,
                    "What is the default value of an uninitialized boolean variable in Java?",
                    "true",
                    "false",
                    "null",
                    "0",
                    "B",
                    10
            ));

            questionRepository.save(new Question(
                    javaExam,
                    "Which Java collection class allows null elements and is synchronized?",
                    "ArrayList",
                    "HashMap",
                    "Vector",
                    "HashSet",
                    "C",
                    10
            ));

            questionRepository.save(new Question(
                    javaExam,
                    "Which exception is thrown when an array is accessed with an illegal index?",
                    "NullPointerException",
                    "IllegalArgumentException",
                    "ArrayIndexOutOfBoundsException",
                    "ClassCastException",
                    "C",
                    10
            ));

            questionRepository.save(new Question(
                    javaExam,
                    "What does the JVM do during bytecode execution?",
                    "Directly executes hardware instructions",
                    "Translates bytecode into machine code via JIT and interpreter",
                    "Compiles source code into .class files",
                    "Minifies Java code",
                    "B",
                    10
            ));

            // Exam 2: DBMS Exam
            Exam dbmsExam = new Exam(
                    "Database Management Systems (DBMS)",
                    "Relational database concepts, SQL queries, Normalization, ACID properties and Indexing.",
                    "Database Systems",
                    LocalDate.now(),
                    LocalTime.of(10, 0),
                    30,
                    40,
                    16,
                    ExamStatus.ACTIVE
            );
            dbmsExam = examRepository.save(dbmsExam);

            questionRepository.save(new Question(
                    dbmsExam,
                    "What does ACID stand for in Database Systems?",
                    "Atomicity, Consistency, Isolation, Durability",
                    "Accuracy, Consistency, Integrity, Dependency",
                    "Atomicity, Concurrency, Isolation, Durability",
                    "Association, Consistency, Indexing, Data",
                    "A",
                    10
            ));

            questionRepository.save(new Question(
                    dbmsExam,
                    "Which SQL clause is used to filter groups of records created by GROUP BY?",
                    "WHERE",
                    "ORDER BY",
                    "HAVING",
                    "FILTER",
                    "C",
                    10
            ));

            questionRepository.save(new Question(
                    dbmsExam,
                    "Which normal form eliminates transitive functional dependency?",
                    "1NF",
                    "2NF",
                    "3NF",
                    "BCNF",
                    "C",
                    10
            ));

            questionRepository.save(new Question(
                    dbmsExam,
                    "Which type of JOIN returns all records when there is a match in either left or right table?",
                    "INNER JOIN",
                    "LEFT JOIN",
                    "RIGHT JOIN",
                    "FULL OUTER JOIN",
                    "D",
                    10
            ));

            // Exam 3: Web Development (Upcoming)
            Exam webExam = new Exam(
                    "Web Development & REST APIs",
                    "Fundamentals of HTTP, HTML5, JavaScript, React components, and RESTful web services.",
                    "Web Technologies",
                    LocalDate.now().plusDays(2),
                    LocalTime.of(14, 0),
                    60,
                    60,
                    24,
                    ExamStatus.UPCOMING
            );
            examRepository.save(webExam);

            // Exam 4: Data Structures (Completed)
            Exam dsaExam = new Exam(
                    "Data Structures & Algorithms",
                    "Arrays, Linked Lists, Stacks, Queues, Trees, Graphs, Sorting and Big-O notation.",
                    "Computer Science",
                    LocalDate.now().minusDays(5),
                    LocalTime.of(11, 0),
                    60,
                    50,
                    25,
                    ExamStatus.COMPLETED
            );
            examRepository.save(dsaExam);

            logger.info("Initialized default sample examinations and questions.");
        }
    }
}
