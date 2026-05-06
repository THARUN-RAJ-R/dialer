package com.dialer.backend.dto;

/**
 * Response DTO sent back to the Android app for a spam check.
 * Matches the Android Retrofit model: SpamResponse(number, is_spam, score)
 */
public class SpamCheckResponse {

    private String number;
    private boolean is_spam;
    private int score;

    public SpamCheckResponse() {}

    public SpamCheckResponse(String number, boolean is_spam, int score) {
        this.number = number;
        this.is_spam = is_spam;
        this.score = score;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public boolean is_spam() { return is_spam; }
    public void setIs_spam(boolean is_spam) { this.is_spam = is_spam; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
