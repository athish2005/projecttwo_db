package com.example.projectthree.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectthree.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
        Optional<Payment> findByBookingId(Long bookingId);
        boolean existsByBookingId(Long bookingId);


    
}
