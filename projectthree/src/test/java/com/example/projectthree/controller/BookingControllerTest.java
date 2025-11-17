package com.example.projectthree.controller;


import com.example.projectthree.entity.*;
import com.example.projectthree.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;
    private User user;
    private Room room;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = Hotel.builder()
                .id(1L)
                .name("Ocean View Hotel")
                .city("Chennai")
                .address("ECR Road")
                .build();

        room = Room.builder()
                .id(1L)
                .roomNumber("101")
                .roomType("Deluxe")
                .price(2500.0)
                .hotel(hotel)
                .build();

        user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .role(User.Role.USER)
                .build();

        booking = Booking.builder()
                .id(10L)
                .user(user)
                .room(room)
                .checkInDate(LocalDate.of(2025, 11, 11))
                .checkOutDate(LocalDate.of(2025, 11, 13))
                .status(Booking.BookingStatus.CONFIRMED)
                .totalPrice(5000.0)
                .build();
    }

    // ✅ Create booking
    @Test
    void testCreateBooking() {
        when(bookingService.createBooking(1L, 1L,
                LocalDate.of(2025, 11, 11), LocalDate.of(2025, 11, 13)))
                .thenReturn(booking);

        Booking result = bookingController.createBooking(1L, 1L, "2025-11-11", "2025-11-13");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(bookingService, times(1)).createBooking(anyLong(), anyLong(), any(), any());
    }

    // ✅ Get all bookings
    @Test
    void testGetAllBookings() {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        List<?> result = bookingController.getAllBookings();

        assertEquals(1, result.size());
        verify(bookingService, times(1)).getAllBookings();
    }

    // ✅ Get bookings by user
    @Test
    void testGetBookingsByUser() {
        when(bookingService.getBookingsByUser(1L)).thenReturn(List.of(booking));

        List<?> result = bookingController.getBookingsByUser(1L);

        assertEquals(1, result.size());
        verify(bookingService, times(1)).getBookingsByUser(1L);
    }

    // ✅ Cancel booking
    @Test
    void testCancelBooking() {
        doNothing().when(bookingService).cancelBooking(10L);

        String result = bookingController.cancelBooking(10L);

        assertEquals("Booking cancelled successfully", result);
        verify(bookingService, times(1)).cancelBooking(10L);
    }
}

