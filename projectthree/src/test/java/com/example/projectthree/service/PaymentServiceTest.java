package com.example.projectthree.service;


import com.example.projectthree.entity.*;
import com.example.projectthree.entity.Payment.PaymentStatus;
import com.example.projectthree.repository.BookingRepository;
import com.example.projectthree.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Booking booking;
    private Payment payment;
    private Map<String, Object> stripeResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = Booking.builder()
                .id(1L)
                .status(Booking.BookingStatus.CONFIRMED)
                .build();

        payment = Payment.builder()
                .id(1L)
                .amount(5000.0)
                .status(PaymentStatus.SUCCESS)
                .transactionId("txn_12345")
                .booking(booking)
                .build();

        stripeResponse = Map.of(
                "amount", 5000.0,
                "id", "txn_12345",
                "status", "succeeded"
        );
    }

    // ✅ Save payment successfully
    @Test
    void testSaveStripePayment_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingId(1L)).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.saveStripePayment(1L, stripeResponse);

        assertNotNull(result);
        assertEquals("txn_12345", result.getTransactionId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    // ❌ Test duplicate payment prevention
    @Test
    void testSaveStripePayment_Duplicate() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBookingId(1L)).thenReturn(true);
        when(paymentRepository.findByBookingId(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.saveStripePayment(1L, stripeResponse);

        assertEquals("txn_12345", result.getTransactionId());
        verify(paymentRepository, never()).save(any());
    }

    // ❌ Test missing booking
    @Test
    void testSaveStripePayment_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                paymentService.saveStripePayment(1L, stripeResponse)
        );

        assertEquals("Booking not found", ex.getMessage());
    }

    // ✅ Get payment by booking ID success
    @Test
    void testGetPaymentByBookingId_Success() {
        when(paymentRepository.findByBookingId(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentByBookingId(1L);

        assertEquals(5000.0, result.getAmount());
    }

    // ❌ Get payment by booking ID not found
    @Test
    void testGetPaymentByBookingId_NotFound() {
        when(paymentRepository.findByBookingId(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                paymentService.getPaymentByBookingId(1L)
        );

        assertTrue(ex.getMessage().contains("No payment found"));
    }
}
