package com.example.chattest.modules.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponseDto {

    private String accessToken;

    private String refreshToken;

    public JwtTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
