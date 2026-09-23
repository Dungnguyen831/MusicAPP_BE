package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private UserInfo user;

    @Data
    @Builder
    public static class UserInfo {
        private Long id;
        private String email;
        private String username;
        private String fullName;
        private String avatarUrl;
        private List<String> roles;
    }
}
