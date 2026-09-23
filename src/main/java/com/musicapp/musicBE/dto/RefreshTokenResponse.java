package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
}
