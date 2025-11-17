package com.example.projectthree.service;


import com.example.projectthree.entity.Hotel;
import com.example.projectthree.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotel = Hotel.builder()
                .id(1L)
                .name("Ocean View")
                .city("Chennai")
                .address("ECR Road")
                .description("Sea-facing hotel")
                .rating(4.5)
                .build();
    }

    // ✅ Add hotel
    @Test
    void testAddHotel() {
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel result = hotelService.addHotel(hotel);

        assertNotNull(result);
        assertEquals("Ocean View", result.getName());
        verify(hotelRepository, times(1)).save(hotel);
    }

    // ✅ Update hotel
    @Test
    void testUpdateHotel() {
        Hotel updated = Hotel.builder()
                .name("Updated Hotel")
                .city("Mumbai")
                .address("Marine Drive")
                .description("Luxury Stay")
                .rating(5.0)
                .build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(updated);

        Hotel result = hotelService.updateHotel(1L, updated);

        assertEquals("Updated Hotel", result.getName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    // ✅ Delete hotel
    @Test
    void testDeleteHotel() {
        doNothing().when(hotelRepository).deleteById(1L);

        hotelService.deleteHotel(1L);

        verify(hotelRepository, times(1)).deleteById(1L);
    }

    // ✅ Get all hotels
    @Test
    void testGetAllHotels() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        List<Hotel> result = hotelService.getAllHotels();

        assertEquals(1, result.size());
        verify(hotelRepository, times(1)).findAll();
    }

    // ✅ Get hotel by ID
    @Test
    void testGetHotelById() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getHotelById(1L);

        assertEquals("Ocean View", result.getName());
    }

    // ✅ Search hotels by city
    @Test
    void testSearchHotelsByCity() {
        when(hotelRepository.findByCityContainingIgnoreCase("Chennai")).thenReturn(List.of(hotel));

        List<Hotel> result = hotelService.searchHotelsByCity("Chennai");

        assertEquals(1, result.size());
        verify(hotelRepository, times(1)).findByCityContainingIgnoreCase("Chennai");
    }
}

