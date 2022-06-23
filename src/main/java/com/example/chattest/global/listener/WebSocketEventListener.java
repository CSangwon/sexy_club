package com.example.chattest.global.listener;

import com.example.chattest.modules.chat.dto.request.ChatMessage;
import com.example.chattest.modules.chat.entity.MessageType;
import com.example.chattest.modules.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatRoomService chatRoomService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String email = (String) headerAccessor.getSessionAttributes().get("email");
        Long chatRoomId = (Long) headerAccessor.getSessionAttributes().get("chatRoomId");

        if (email != null && chatRoomId != null) {
            chatRoomService.exit(chatRoomId);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(email);

            simpMessageSendingOperations.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
        }
    }
}
