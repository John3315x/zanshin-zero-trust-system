package com.nanakusa.zanshin.dto;

import jakarta.validation.constraints.NotBlank;

public class SessionRequest {

    @NotBlank(message = "Token is required")
    private String plainRefreshToken;

    public String getPlainRefreshToken() {
        return plainRefreshToken;
    }
}
