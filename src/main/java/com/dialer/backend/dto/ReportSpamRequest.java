package com.dialer.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO received from the Android app when a user reports a number.
 * Matches the Android Retrofit model: ReportSpamRequest(number, is_spam)
 */
public class ReportSpamRequest {

    @NotBlank(message = "Phone number must not be blank")
    private String number;

    private boolean is_spam;

    public ReportSpamRequest() {}

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public boolean is_spam() { return is_spam; }
    public void setIs_spam(boolean is_spam) { this.is_spam = is_spam; }
}
