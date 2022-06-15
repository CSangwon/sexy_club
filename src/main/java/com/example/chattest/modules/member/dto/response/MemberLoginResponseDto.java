package com.example.chattest.modules.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Long id;

    private String email;

    private String accessToken;

    private String refreshToken;
}
