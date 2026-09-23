package com.musicapp.musicBE.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
