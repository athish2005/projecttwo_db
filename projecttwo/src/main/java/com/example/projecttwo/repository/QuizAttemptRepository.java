package com.example.projecttwo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projecttwo.entity.QuizAttempt;
import com.example.projecttwo.entity.User;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt,Long>{

    List<QuizAttempt> findByUser(User user);
    
}
