package com.example.projecttwo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projecttwo.entity.UserAnswer;
import com.example.projecttwo.repository.UserAnswerRepository;

import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public List<UserAnswer> saveAllAnswers(List<UserAnswer> answers) {
        return userAnswerRepository.saveAll(answers);
    }

    public List<UserAnswer> getAllAnswers() {
        return userAnswerRepository.findAll();
    }
}

