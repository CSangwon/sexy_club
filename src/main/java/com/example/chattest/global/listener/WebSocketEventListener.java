package com.example.chattest.global.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatRoomService chatRoomService;


}
