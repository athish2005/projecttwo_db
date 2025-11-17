package com.example.projectthree.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectthree.entity.Hotel;
import com.example.projectthree.repository.HotelRepository;

import java.util.List;



@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    // ✅ Add new hotel (Admin only)
    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // ✅ Update hotel
    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Hotel existing = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        existing.setName(updatedHotel.getName());
        existing.setCity(updatedHotel.getCity());
        existing.setAddress(updatedHotel.getAddress());
        existing.setDescription(updatedHotel.getDescription());
        existing.setRating(updatedHotel.getRating());
        return hotelRepository.save(existing);
    }

    // ✅ Delete hotel
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    // ✅ Get all hotels
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // ✅ Get hotel by ID
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
    }

    // ✅ Search hotels by city
    public List<Hotel> searchHotelsByCity(String city) {
        return hotelRepository.findByCityContainingIgnoreCase(city);
    }
}

