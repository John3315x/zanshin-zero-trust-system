package com.nanakusa.zanshin.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String email;

    public AuthResponse(String accessToken, String refreshToken, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
    }

    // Solo para produccion ❗
    public AuthResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
