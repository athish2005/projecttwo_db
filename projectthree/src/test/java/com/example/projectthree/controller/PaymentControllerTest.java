package com.example.projectthree.controller;


import com.example.projectthree.entity.Payment;
import com.example.projectthree.entity.Payment.PaymentStatus;
import com.example.projectthree.service.PaymentService;
import com.example.projectthree.service.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private StripeService stripeService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private Map<String, Object> stripeResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        stripeResponse = Map.of(
                "amount", 5000.0,
                "currency", "inr",
                "status", "success",
                "clientSecret", "dummy_secret"
        );

        payment = Payment.builder()
                .id(1L)
                .amount(5000.0)
                .transactionId("txn_12345")
                .status(PaymentStatus.SUCCESS)
                .build();
    }

    // ✅ Test successful payment creation
    @Test
    void testCreatePayment_Success() {
        when(stripeService.createStripePayment(1L)).thenReturn(stripeResponse);
        when(paymentService.saveStripePayment(1L, stripeResponse)).thenReturn(payment);

        ResponseEntity<?> response = paymentController.createPayment(1L);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("txn_12345", body.get("transactionId"));
        assertEquals("SUCCESS", body.get("status"));
        assertEquals(5000.0, body.get("amount"));
        verify(stripeService, times(1)).createStripePayment(1L);
        verify(paymentService, times(1)).saveStripePayment(1L, stripeResponse);
    }

    // ❌ Test payment creation failure
    @Test
    void testCreatePayment_Failure() {
        when(stripeService.createStripePayment(1L)).thenThrow(new RuntimeException("Stripe error"));

        ResponseEntity<?> response = paymentController.createPayment(1L);

        assertEquals(500, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(((String) body.get("error")).contains("Stripe error"));
    }

    // ✅ Test get payment by booking ID success
    @Test
    void testGetPaymentByBooking_Success() {
        when(paymentService.getPaymentByBookingId(1L)).thenReturn(payment);

        ResponseEntity<?> response = paymentController.getPaymentByBooking(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payment, response.getBody());
    }

    // ❌ Test get payment by booking ID not found
    @Test
    void testGetPaymentByBooking_NotFound() {
        when(paymentService.getPaymentByBookingId(1L))
                .thenThrow(new RuntimeException("Payment not found"));

        ResponseEntity<?> response = paymentController.getPaymentByBooking(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Payment not found", response.getBody());
    }
}

