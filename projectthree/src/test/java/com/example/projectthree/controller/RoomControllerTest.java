package com.example.projectthree.controller;


import com.example.projectthree.entity.Hotel;
import com.example.projectthree.entity.Room;
import com.example.projectthree.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room room;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = Hotel.builder()
                .id(1L)
                .name("Blue Sky Hotel")
                .city("Chennai")
                .build();

        room = Room.builder()
                .id(2L)
                .roomNumber("202")
                .roomType("DELUXE")
                .price(3000.0)
                .hotel(hotel)
                .available(true)
                .build();
    }

    // ✅ Add room
    @Test
    void testAddRoom() {
        when(roomService.addRoom(1L, room)).thenReturn(room);

        Room result = roomController.addRoom(1L, room);

        assertNotNull(result);
        assertEquals("DELUXE", result.getRoomType());
        verify(roomService, times(1)).addRoom(1L, room);
    }

    // ✅ Get rooms by hotel
    @Test
    void testGetRoomsByHotel() {
        when(roomService.getRoomsByHotel(1L)).thenReturn(List.of(room));

        List<Room> result = roomController.getRoomsByHotel(1L);

        assertEquals(1, result.size());
        verify(roomService, times(1)).getRoomsByHotel(1L);
    }

    // ✅ Get room by ID
    @Test
    void testGetRoomById() {
        when(roomService.getRoomById(2L)).thenReturn(room);

        Room result = roomController.getRoomById(2L);

        assertEquals("202", result.getRoomNumber());
        verify(roomService, times(1)).getRoomById(2L);
    }
}
