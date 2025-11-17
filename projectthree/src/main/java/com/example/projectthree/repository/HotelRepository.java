package com.example.projectthree.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectthree.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
        List<Hotel> findByCityContainingIgnoreCase(String city);

    
}
