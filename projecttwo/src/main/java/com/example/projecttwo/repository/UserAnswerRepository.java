package com.example.projecttwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projecttwo.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
    
}
