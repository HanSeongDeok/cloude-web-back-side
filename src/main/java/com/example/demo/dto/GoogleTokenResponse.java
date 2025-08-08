package com.example.demo.dto;

import lombok.Data;

@Data
public class GoogleTokenResponse {
    private String access_token;
    private String refresh_token;
    private String id_token;
    private String token_type;
    private Long expires_in;
}