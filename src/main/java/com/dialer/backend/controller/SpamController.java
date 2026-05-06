package com.dialer.backend.controller;

import com.dialer.backend.dto.ReportSpamRequest;
import com.dialer.backend.dto.SpamCheckResponse;
import com.dialer.backend.service.SpamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller — the HTTP entry point for the Android app.
 *
 * Endpoints:
 *   GET  /api/spam/check?number={phone}  → Check if a number is spam
 *   POST /api/spam/report                → Report a number as spam or safe
 */
@RestController
@RequestMapping("/api/spam")
public class SpamController {

    private final SpamService spamService;

    public SpamController(SpamService spamService) {
        this.spamService = spamService;
    }

    /**
     * Check if a phone number is spam.
     *
     * Example: GET /api/spam/check?number=9876543210
     * Response: { "number": "9876543210", "is_spam": true, "score": 87 }
     */
    @GetMapping("/check")
    public ResponseEntity<SpamCheckResponse> checkSpam(@RequestParam("number") String number) {
        if (number == null || number.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        SpamCheckResponse response = spamService.checkSpam(number);
        return ResponseEntity.ok(response);
    }

    /**
     * Report a phone number as spam or safe.
     *
     * Example: POST /api/spam/report
     * Body: { "number": "9876543210", "is_spam": true }
     * Response: 200 OK (no body)
     */
    @PostMapping("/report")
    public ResponseEntity<Void> reportSpam(@Valid @RequestBody ReportSpamRequest request) {
        spamService.reportNumber(request);
        return ResponseEntity.ok().build();
    }
}
