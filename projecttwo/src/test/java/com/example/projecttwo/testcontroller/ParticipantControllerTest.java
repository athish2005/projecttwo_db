package com.example.projecttwo.testcontroller;


import com.example.projecttwo.controller.ParticipantController;
import com.example.projecttwo.entity.Question;
import com.example.projecttwo.entity.Quiz;
import com.example.projecttwo.service.QuestionService;
import com.example.projecttwo.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ParticipantController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ Disable Spring Security filters during test
public class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private QuestionService questionService;

    private Quiz quiz;
    private Question question;

    @BeforeEach
    void setup() {
        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Basics");

        question = new Question();
        question.setId(1L);
        question.setQuestionText("What is JVM?");
    }

    // ✅ Test: Get all quizzes
    @Test
    void testGetAllQuizzes() throws Exception {
        List<Quiz> quizzes = Arrays.asList(quiz);
        Mockito.when(quizService.getAllQuizzes()).thenReturn(quizzes);

        mockMvc.perform(get("/api/participant/quizzes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Basics"));
    }

    // ✅ Test: Get quiz by ID
    @Test
    void testGetQuizById() throws Exception {
        Mockito.when(quizService.getQuizById(1L)).thenReturn(quiz);

        mockMvc.perform(get("/api/participant/quiz/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Basics"));
    }

    // ✅ Test: Get questions for quiz
    @Test
    void testGetQuestionsForQuiz() throws Exception {
        List<Question> questions = Arrays.asList(question);
        Mockito.when(questionService.getQuestionsByQuizId(anyLong())).thenReturn(questions);

        mockMvc.perform(get("/api/participant/quiz/1/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("What is JVM?"));
    }
}
