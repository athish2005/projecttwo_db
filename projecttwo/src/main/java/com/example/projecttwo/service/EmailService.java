package com.example.projecttwo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    public void sendEmail(String to, String subject, String message) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> body = new HashMap<>();
            Map<String, String> sender = new HashMap<>();
            sender.put("email", "athish042005@gmail.com"); // the same email you verified in Brevo

            body.put("sender", sender);
            body.put("to", new Object[]{ Map.of("email", to) });
            body.put("subject", subject);
            body.put("htmlContent", "<p>" + message + "</p>");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Email sent successfully via Brevo API");
            } else {
                System.err.println("❌ Email failed: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("❌ Email sending failed via Brevo API: " + e.getMessage());
        }
    }
}
