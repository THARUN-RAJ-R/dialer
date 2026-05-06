package com.dialer.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Database table: spam_entries
 * Represents a phone number and its spam classification.
 */
@Entity
@Table(name = "spam_entries")
public class SpamEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "is_spam", nullable = false)
    private boolean isSpam;

    // Score from 0 (safe) to 100 (definitely spam)
    @Column(name = "score", nullable = false)
    private int score;

    // How many users reported this number
    @Column(name = "report_count", nullable = false)
    private int reportCount = 0;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // ─── Constructors ────────────────────────────────────────────────────────

    public SpamEntry() {}

    public SpamEntry(String phoneNumber, boolean isSpam, int score) {
        this.phoneNumber = phoneNumber;
        this.isSpam = isSpam;
        this.score = score;
        this.reportCount = 1;
        this.lastUpdated = LocalDateTime.now();
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isSpam() { return isSpam; }
    public void setSpam(boolean spam) { isSpam = spam; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getReportCount() { return reportCount; }
    public void setReportCount(int reportCount) { this.reportCount = reportCount; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
