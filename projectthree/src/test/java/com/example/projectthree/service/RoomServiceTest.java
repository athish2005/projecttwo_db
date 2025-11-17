package com.example.projectthree.service;


import com.example.projectthree.entity.Hotel;
import com.example.projectthree.entity.Room;
import com.example.projectthree.repository.HotelRepository;
import com.example.projectthree.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = Hotel.builder()
                .id(1L)
                .name("Blue Sky Hotel")
                .city("Chennai")
                .address("ECR Road")
                .build();

        room = Room.builder()
                .id(1L)
                .roomNumber("101")
                .roomType("DELUXE")
                .price(2500.0)
                .available(true)
                .hotel(hotel)
                .build();
    }

    // ✅ Test add room success
    @Test
    void testAddRoom_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room result = roomService.addRoom(1L, room);

        assertNotNull(result);
        assertEquals("DELUXE", result.getRoomType());
        verify(hotelRepository, times(1)).findById(1L);
        verify(roomRepository, times(1)).save(room);
    }

    // ❌ Test add room when hotel not found
    @Test
    void testAddRoom_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                roomService.addRoom(1L, room)
        );

        assertEquals("Hotel not found", ex.getMessage());
        verify(roomRepository, never()).save(any());
    }

    // ✅ Test get rooms by hotel
    @Test
    void testGetRoomsByHotel() {
        when(roomRepository.findByHotelId(1L)).thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByHotel(1L);

        assertEquals(1, result.size());
        assertEquals("101", result.get(0).getRoomNumber());
        verify(roomRepository, times(1)).findByHotelId(1L);
    }

    // ✅ Test get room by ID success
    @Test
    void testGetRoomById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);

        assertEquals("DELUXE", result.getRoomType());
        verify(roomRepository, times(1)).findById(1L);
    }

    // ❌ Test get room by ID not found
    @Test
    void testGetRoomById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                roomService.getRoomById(1L)
        );

        assertEquals("Room not found", ex.getMessage());
    }
}
