package com.example.chattest.modules.chat.service;

import com.example.chattest.global.advice.exception.ChatRoomNameInvalidException;
import com.example.chattest.modules.chat.dto.response.ChatRoomResponse;
import com.example.chattest.modules.chat.entity.ChatRoom;
import com.example.chattest.modules.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public void save(String name) {
        if (name == null || name.isBlank()) {
            throw new ChatRoomNameInvalidException("유효하지 않은 채팅방 이름입니다.");
        }
        chatRoomRepository.save(new ChatRoom(name));
    }

    public List<ChatRoomResponse> findAll() {
        return chatRoomRepository.findAll().stream()
                .map((chatRoom) -> new ChatRoomResponse(chatRoom))
                        .collect(Collectors.toList());
    }

    public void deleteByCreatedDateLessThanEqual() {
        chatRoomRepository.findAll().stream().forEach((chatRoom -> {
            if (chatRoom.isRemovable()) {
                chatRoomRepository.deleteById(chatRoom.getId());
            }
        }));
    }

    public void enter(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        chatRoom.enter();
    }

    public void exit(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        chatRoom.exit();
    }

}
