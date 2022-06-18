package com.example.chattest.modules;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class testController {

    @GetMapping("/test")
    public String testPage(@AuthenticationPrincipal String email) {
        log.info(email);
        return "hello";
    }
}
