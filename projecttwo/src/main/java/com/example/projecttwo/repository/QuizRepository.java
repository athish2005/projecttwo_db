package com.example.projecttwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projecttwo.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    
}
