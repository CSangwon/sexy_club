package com.example.chattest.modules.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomStateService {

    private final ChatRoomService chatRoomService;

    @Scheduled(cron = "0/1 * * * * *")
    public void deleteRooms(){
        chatRoomService.deleteByCreatedDateLessThanEqual();
    }
}
