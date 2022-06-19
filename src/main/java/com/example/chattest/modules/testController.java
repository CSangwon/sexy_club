package com.example.chattest.modules;


import com.example.chattest.modules.member.entity.CurrentMember;
import com.example.chattest.modules.member.entity.Member;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class testController {

    @GetMapping("/test")
    public String testPage(@AuthenticationPrincipal String email){
        log.info(email);
        return email;
    }
}
