package com.example.projecttwo.testservice;


import com.example.projecttwo.entity.Question;
import com.example.projecttwo.entity.Quiz;
import com.example.projecttwo.repository.QuestionRepository;
import com.example.projecttwo.service.QuestionService;
import com.example.projecttwo.service.QuizService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuestionService questionService;

    private Quiz quiz;
    private Question question;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Spring Boot Quiz");

        question = new Question();
        question.setId(101L);
        question.setQuestionText("What is Spring Boot?");
        question.setCorrectAnswer("A framework");
        question.setQuiz(quiz);
    }

    @Test
    void testAddQuestion() {
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question saved = questionService.addQuestion(question);

        assertNotNull(saved);
        assertEquals("Spring Boot Quiz", saved.getQuiz().getTitle());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void testAddQuestionToQuiz() {
        when(quizService.getQuizById(1L)).thenReturn(quiz);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question saved = questionService.addQuestionToQuiz(1L, question);

        assertEquals(quiz, saved.getQuiz());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void testGetQuestionsByQuizId() {
        when(questionRepository.findByQuizId(1L)).thenReturn(List.of(question));

        List<Question> result = questionService.getQuestionsByQuizId(1L);

        assertEquals(1, result.size());
        assertEquals("What is Spring Boot?", result.get(0).getQuestionText());
    }

    @Test
    void testDeleteQuestion() {
        doNothing().when(questionRepository).deleteById(101L);

        questionService.deleteQuestion(101L);

        verify(questionRepository, times(1)).deleteById(101L);
    }
}

