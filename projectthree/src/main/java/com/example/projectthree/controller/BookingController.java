package com.example.projectthree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.projectthree.entity.Booking;
import com.example.projectthree.service.BookingService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // ✅ Create a booking
    @PostMapping
    public Booking createBooking(
            @RequestParam Long userId,
            @RequestParam Long roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut
    ) {
        LocalDate checkInDate = LocalDate.parse(checkIn.trim());
        LocalDate checkOutDate = LocalDate.parse(checkOut.trim());
        return bookingService.createBooking(userId, roomId, checkInDate, checkOutDate);
    }

    // ✅ Get all bookings (Admin dashboard)
    @GetMapping
    public List<Map<String, Object>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return mapBookingsToResponse(bookings);
    }

    // ✅ Get bookings for a specific user (User dashboard)
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getBookingsByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return mapBookingsToResponse(bookings);
    }

    // ✅ Cancel a booking
    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking cancelled successfully";
    }

    // 🔧 Common method to flatten data
    private List<Map<String, Object>> mapBookingsToResponse(List<Booking> bookings) {
        return bookings.stream().map(b -> {
            Map<String, Object> bookingData = new LinkedHashMap<>();

            bookingData.put("id", b.getId());
            bookingData.put("userEmail",
                    b.getUser() != null ? b.getUser().getEmail() : "Unknown");

            bookingData.put("hotelName",
                    (b.getRoom() != null && b.getRoom().getHotel() != null)
                            ? b.getRoom().getHotel().getName()
                            : "N/A");

            bookingData.put("roomNumber",
                    b.getRoom() != null ? b.getRoom().getRoomNumber() : "N/A");

            bookingData.put("checkInDate", b.getCheckInDate());
            bookingData.put("checkOutDate", b.getCheckOutDate());
            bookingData.put("status", b.getStatus());
            Double totalPrice = b.getTotalPrice();
            bookingData.put("amount", totalPrice);

            bookingData.put("paymentStatus",
                    b.getPayment() != null ? b.getPayment().getStatus() : "N/A");

            return bookingData;
        }).collect(Collectors.toList());
    }
}



