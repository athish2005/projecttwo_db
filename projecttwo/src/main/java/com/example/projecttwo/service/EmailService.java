package com.example.projecttwo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;  // from env

    @Value("${BREVO_SENDER}")
    private String senderEmail;  // from env (must be verified in Brevo)

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(String to, String subject, String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Build body
            Map<String, Object> body = new HashMap<>();
            Map<String, String> sender = new HashMap<>();
            Map<String, String> recipient = new HashMap<>();

            sender.put("email", senderEmail);
            recipient.put("email", to);

            body.put("sender", sender);
            body.put("to", new Map[] { recipient });
            body.put("subject", subject);
            body.put("textContent", message);
            // If you want HTML instead: body.put("htmlContent", "<h1>Hello</h1><p>...</p>");

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                restTemplate.exchange(BREVO_URL, HttpMethod.POST, request, String.class);

            System.out.println("✅ Email sent via Brevo API: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("❌ Email sending failed via Brevo API: " + e.getMessage());
        }
    }
}

