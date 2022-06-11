package com.example.chattest.modules.member.dto.request;

import lombok.Data;

@Data
public class EmailAuthRequestDto {

    private String email;

    private String expireToken;
}
