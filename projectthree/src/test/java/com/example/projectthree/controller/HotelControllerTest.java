package com.example.projectthree.controller;


import com.example.projectthree.entity.Hotel;
import com.example.projectthree.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = Hotel.builder()
                .id(1L)
                .name("Ocean View Hotel")
                .city("Chennai")
                .address("ECR Road")
                .description("Sea view hotel")
                .build();
    }

    // ✅ Add hotel
    @Test
    void testAddHotel() {
        when(hotelService.addHotel(hotel)).thenReturn(hotel);

        Hotel result = hotelController.addHotel(hotel);

        assertNotNull(result);
        assertEquals("Ocean View Hotel", result.getName());
        verify(hotelService, times(1)).addHotel(hotel);
    }

    // ✅ Update hotel
    @Test
    void testUpdateHotel() {
        when(hotelService.updateHotel(1L, hotel)).thenReturn(hotel);

        Hotel result = hotelController.updateHotel(1L, hotel);

        assertEquals(1L, result.getId());
        verify(hotelService, times(1)).updateHotel(1L, hotel);
    }

    // ✅ Delete hotel
    @Test
    void testDeleteHotel() {
        doNothing().when(hotelService).deleteHotel(1L);

        String result = hotelController.deleteHotel(1L);

        assertEquals("Hotel deleted successfully", result);
        verify(hotelService, times(1)).deleteHotel(1L);
    }

    // ✅ Get all hotels
    @Test
    void testGetAllHotels() {
        when(hotelService.getAllHotels()).thenReturn(List.of(hotel));

        List<Hotel> result = hotelController.getAllHotels();

        assertEquals(1, result.size());
        verify(hotelService, times(1)).getAllHotels();
    }

    // ✅ Get hotel by ID
    @Test
    void testGetHotelById() {
        when(hotelService.getHotelById(1L)).thenReturn(hotel);

        Hotel result = hotelController.getHotelById(1L);

        assertNotNull(result);
        assertEquals("Ocean View Hotel", result.getName());
        verify(hotelService, times(1)).getHotelById(1L);
    }

    // ✅ Search hotels by city
    @Test
    void testSearchHotelsByCity() {
        when(hotelService.searchHotelsByCity("Chennai")).thenReturn(List.of(hotel));

        List<Hotel> result = hotelController.searchHotelsByCity("Chennai");

        assertEquals(1, result.size());
        verify(hotelService, times(1)).searchHotelsByCity("Chennai");
    }
}
