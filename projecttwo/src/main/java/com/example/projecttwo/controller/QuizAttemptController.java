package com.example.projecttwo.controller;

import com.example.projecttwo.entity.QuizAttempt;
import com.example.projecttwo.entity.User;
import com.example.projecttwo.entity.Quiz;
import com.example.projecttwo.service.EmailService;
import com.example.projecttwo.service.QuizAttemptService;
import com.example.projecttwo.service.QuizService;
import com.example.projecttwo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class QuizAttemptController {

    @Autowired private QuizAttemptService attemptService;
    @Autowired private UserService userService;
    @Autowired private QuizService quizService;
    @Autowired private EmailService emailService;

    // Submit attempt (you can submit either full QuizAttempt JSON or use query params)
   @PostMapping("/submit")
    public ResponseEntity<QuizAttempt> submitAttempt(@RequestBody QuizAttempt attempt) {
    User user = userService.findByEmail(attempt.getUser().getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
    Quiz quiz = quizService.getQuizById(attempt.getQuiz().getId());

    // Create and save attempt
    QuizAttempt newAttempt = QuizAttempt.builder()
            .user(user)
            .quiz(quiz)
            .score(attempt.getScore())
            .attemptedAt(LocalDateTime.now())
            .build();

    QuizAttempt savedAttempt = attemptService.saveAttempt(newAttempt);

    // Send result email
    String subject = "Your Quiz Results - " + quiz.getTitle();
    String message = "Hi " + user.getName() + ",\n\n"
            + "You’ve completed the quiz: *" + quiz.getTitle() + "* 🎉\n"
            + "Your score: " + attempt.getScore() + " / " + quiz.getQuestions().size() + " points.\n\n"
            + "Keep practicing and good luck for your next quiz!\n\n"
            + "- Quiz Platform Team";

    emailService.sendEmail(user.getEmail(), subject, message);

    return ResponseEntity.ok(savedAttempt);
   }

    // Alternative submit by params (email + quizId + score)
    @PostMapping("/submit/by")
    public ResponseEntity<?> submitByParams(@RequestParam String email,
                                            @RequestParam Long quizId,
                                            @RequestParam int score) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizService.getQuizById(quizId);
        QuizAttempt saved = attemptService.saveAttempt(user, quiz, score);
        return ResponseEntity.ok(saved);
    }

    // Get attempts for user (by email)
    @GetMapping("/user")
    public ResponseEntity<List<QuizAttempt>> getAttemptsByUser(@RequestParam String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(attemptService.getAttemptsByUser(user));
    }

    // Get all attempts
    @GetMapping("/all")
    public ResponseEntity<List<QuizAttempt>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }
}
