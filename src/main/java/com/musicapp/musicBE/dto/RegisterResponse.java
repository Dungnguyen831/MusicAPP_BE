package com.musicapp.musicBE.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RegisterResponse {
    private Long userId;
    private String email;
    private String username;
    private String fullName;
    private String status;
    private LocalDateTime createdAt;
}
