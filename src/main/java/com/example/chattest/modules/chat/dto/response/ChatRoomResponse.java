package com.example.chattest.modules.chat.dto.response;

import com.example.chattest.modules.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

    private Long id;

    private String name;

    private int count;

    private LocalDateTime expireDate;

    public ChatRoomResponse(ChatRoom chatRoom) {
    }
}
