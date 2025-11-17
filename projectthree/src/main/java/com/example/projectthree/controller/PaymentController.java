package com.example.projectthree.controller;

import com.example.projectthree.entity.Payment;
import com.example.projectthree.service.PaymentService;
import com.example.projectthree.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private PaymentService paymentService;

    // ✅ Create Stripe Payment Intent
   @PostMapping("/create/{bookingId}")
   public ResponseEntity<?> createPayment(@PathVariable Long bookingId) {
    try {
        Map<String, Object> stripeResponse = stripeService.createStripePayment(bookingId);
        Payment payment = paymentService.saveStripePayment(bookingId, stripeResponse);

        // ✅ Return only lightweight map to avoid circular reference
        return ResponseEntity.ok(Map.of(
            "bookingId", bookingId,
            "transactionId", payment.getTransactionId(),
            "amount", payment.getAmount(),
            "status", payment.getStatus().name(),
            "paymentStatus", stripeResponse.get("status")
        ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
    }
}

    // ✅ Get payment details by booking ID
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBooking(@PathVariable Long bookingId) {
        try {
            Payment payment = paymentService.getPaymentByBookingId(bookingId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
}
