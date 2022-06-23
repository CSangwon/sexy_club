package com.example.chattest.modules.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chatGET() {
        log.info("@ChatController, chat Get()");
        //깃 토큰만료
        return "chat";
    }
}
