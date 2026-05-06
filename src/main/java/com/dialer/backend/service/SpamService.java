package com.dialer.backend.service;

import com.dialer.backend.dto.ReportSpamRequest;
import com.dialer.backend.dto.SpamCheckResponse;
import com.dialer.backend.model.SpamEntry;
import com.dialer.backend.repository.SpamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Core business logic for the Spam Intelligence service.
 *
 * Rules:
 * - checkSpam: Look up the number in DB. If found, return its data.
 *              If not found, return UNKNOWN (score=0, is_spam=false).
 * - reportNumber: If number already exists, update it (recalculate score based on reports).
 *                 If new, create a fresh entry.
 */
@Service
public class SpamService {

    private final SpamRepository spamRepository;

    public SpamService(SpamRepository spamRepository) {
        this.spamRepository = spamRepository;
    }

    /**
     * GET /api/spam/check?number={phoneNumber}
     * Checks the database for a known spam record.
     */
    public SpamCheckResponse checkSpam(String phoneNumber) {
        String normalized = normalizeNumber(phoneNumber);
        Optional<SpamEntry> entry = spamRepository.findByPhoneNumber(normalized);

        if (entry.isPresent()) {
            SpamEntry e = entry.get();
            return new SpamCheckResponse(e.getPhoneNumber(), e.isSpam(), e.getScore());
        }

        // Not found in our database → return UNKNOWN (score 0, not spam)
        return new SpamCheckResponse(normalized, false, 0);
    }

    /**
     * POST /api/spam/report
     * Accepts a user report and updates the community spam database.
     *
     * Score calculation:
     * - Every spam report adds weight.
     * - If majority of reports say SPAM → mark as spam with high score.
     * - Score = (spamReports / totalReports) * 100, capped at 100.
     */
    @Transactional
    public void reportNumber(ReportSpamRequest request) {
        String normalized = normalizeNumber(request.getNumber());
        Optional<SpamEntry> existing = spamRepository.findByPhoneNumber(normalized);

        if (existing.isPresent()) {
            SpamEntry entry = existing.get();

            // Increment report count
            entry.setReportCount(entry.getReportCount() + 1);

            // Recalculate: if the new report says spam, push score up; else down
            if (request.is_spam()) {
                // Each spam vote increases score proportionally
                int newScore = Math.min(100, entry.getScore() + (100 / entry.getReportCount()));
                entry.setScore(newScore);
                if (newScore >= 50) {
                    entry.setSpam(true);
                }
            } else {
                // Safe vote decreases score
                int newScore = Math.max(0, entry.getScore() - (100 / entry.getReportCount()));
                entry.setScore(newScore);
                if (newScore < 50) {
                    entry.setSpam(false);
                }
            }

            entry.setLastUpdated(LocalDateTime.now());
            spamRepository.save(entry);

        } else {
            // First report ever for this number
            int score = request.is_spam() ? 75 : 0; // First spam report = 75 score
            SpamEntry newEntry = new SpamEntry(normalized, request.is_spam(), score);
            spamRepository.save(newEntry);
        }
    }

    /**
     * Normalize phone numbers to strip leading '+' and spaces
     * so "+91 9876543210" and "9876543210" match the same record.
     */
    private String normalizeNumber(String number) {
        if (number == null) return "";
        return number.replaceAll("[\\s\\-()]", ""); // strip spaces, dashes, parens
    }
}
