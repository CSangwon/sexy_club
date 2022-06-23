package com.example.chattest.modules.chat.dto.request;

import com.example.chattest.modules.chat.entity.MessageType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatMessage {

    @NotNull(message = "메시지 타입은 필수 값입니다.")
    private MessageType type;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotBlank(message = "송신자는 필수 값입니다.")
    private String sender;

    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email(message = "이메일 값이 올바르지 않습니다.")
    private String email;



}
