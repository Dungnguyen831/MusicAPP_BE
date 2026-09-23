package com.musicapp.musicBE.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
