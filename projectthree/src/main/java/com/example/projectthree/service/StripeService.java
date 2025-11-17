package com.example.projectthree.service;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

    // ✅ Dummy Stripe API simulation
    public Map<String, Object> createStripePayment(Long bookingId) {
        Map<String, Object> response = new HashMap<>();

        // Normally you'd call Stripe API here (using Stripe SDK)
        // For now, we simulate a successful payment intent
        response.put("id", UUID.randomUUID().toString());
        response.put("amount", 2000); // in cents (Stripe style)
        response.put("currency", "INR");
        response.put("status", "succeeded");
        response.put("clientSecret", UUID.randomUUID().toString());

        return response;
    }
}
