package com.example.chattest.modules.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {
    private String accessToken;

    private String refreshToken;
}
