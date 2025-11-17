package com.example.projectthree.service;

import com.example.projectthree.entity.Booking;
import com.example.projectthree.entity.Payment;
import com.example.projectthree.entity.Payment.PaymentStatus;
import com.example.projectthree.repository.BookingRepository;
import com.example.projectthree.repository.PaymentRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ Save Stripe Payment Info
   @Transactional
public Payment saveStripePayment(Long bookingId, Map<String, Object> stripeResponse) {
    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    // 🛡️ Prevent duplicate payment entries
    if (paymentRepository.existsByBookingId(bookingId)) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment already exists"));
    }

    Payment payment = Payment.builder()
            .amount(Double.parseDouble(stripeResponse.get("amount").toString()))
            .status(PaymentStatus.SUCCESS)
            .transactionId(stripeResponse.get("id").toString())
            .booking(booking)
            .build();

    // 💾 Save payment
    paymentRepository.save(payment);

    // 🧾 Update booking status to reflect payment success
    booking.setStatus(Booking.BookingStatus.CONFIRMED);
    bookingRepository.save(booking);

    return payment;
}

    // ✅ Get Payment by Booking ID
    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("No payment found for booking ID: " + bookingId));
    }
}
