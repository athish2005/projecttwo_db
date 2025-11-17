package com.example.projectthree.service;



import com.example.projectthree.entity.Booking;
import com.example.projectthree.entity.Room;
import com.example.projectthree.entity.User;
import com.example.projectthree.repository.BookingRepository;
import com.example.projectthree.repository.RoomRepository;
import com.example.projectthree.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // ✅ Create a new booking
    public Booking createBooking(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Ensure check-out is after check-in
        if (!checkOut.isAfter(checkIn)) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        // Calculate total price based on number of days
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = days * room.getPrice();

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .totalPrice(totalPrice)
                .status(Booking.BookingStatus.CONFIRMED)
                .build();

        return bookingRepository.save(booking);
    }

    // ✅ Get all bookings for a user
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ✅ Cancel a booking
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
     // ✅ NEW: Admin view — return all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
}
