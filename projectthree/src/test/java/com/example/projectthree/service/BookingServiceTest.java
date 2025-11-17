package com.example.projectthree.service;


import com.example.projectthree.entity.*;
import com.example.projectthree.repository.BookingRepository;
import com.example.projectthree.repository.RoomRepository;
import com.example.projectthree.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Room room;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).name("John").email("john@example.com").build();
        room = Room.builder().id(1L).roomNumber("101").roomType("DELUXE").price(2500.0).available(true).build();

        booking = Booking.builder()
                .id(1L)
                .user(user)
                .room(room)
                .checkInDate(LocalDate.of(2025, 11, 10))
                .checkOutDate(LocalDate.of(2025, 11, 12))
                .status(Booking.BookingStatus.CONFIRMED)
                .totalPrice(5000.0)
                .build();
    }

    // ✅ Test booking creation
    @Test
    void testCreateBooking_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(1L, 1L, LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 12));

        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CONFIRMED, result.getStatus());
        verify(bookingRepository, times(1)).save(any());
    }

    // ❌ Test invalid check-out date
    @Test
    void testCreateBooking_InvalidDates() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(1L, 1L, LocalDate.of(2025, 11, 12), LocalDate.of(2025, 11, 10))
        );

        assertEquals("Check-out date must be after check-in date", ex.getMessage());
    }

    // ✅ Test cancel booking
    @Test
    void testCancelBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        verify(bookingRepository, times(1)).save(booking);
        assertEquals(Booking.BookingStatus.CANCELLED, booking.getStatus());
    }

    // ✅ Test get all bookings for user
    @Test
    void testGetBookingsByUser() {
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(booking));

        List<Booking> results = bookingService.getBookingsByUser(1L);

        assertEquals(1, results.size());
        assertEquals(booking.getId(), results.get(0).getId());
    }

    // ✅ Test get all bookings
    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> results = bookingService.getAllBookings();

        assertEquals(1, results.size());
        verify(bookingRepository, times(1)).findAll();
    }
}

