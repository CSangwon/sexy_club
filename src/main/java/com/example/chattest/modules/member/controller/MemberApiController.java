package com.example.chattest.modules.member.controller;

import com.example.chattest.modules.member.MemberJoinRequestValidator;
import com.example.chattest.modules.member.dto.request.EmailAuthRequestDto;
import com.example.chattest.modules.member.dto.request.MemberJoinRequestDto;
import com.example.chattest.modules.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final MemberJoinRequestValidator memberJoinRequestValidator;

    @InitBinder("memberJoinRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberJoinRequestValidator);
    }

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<?> joinMember(@Valid @RequestBody MemberJoinRequestDto memberJoinRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        return memberService.joinMember(memberJoinRequestDto);
    }

    //이메일 중복확인 및 이메일 토큰 생성
    @PostMapping("/join/{email}")
    public void confirmEmailAndCreateEmailToken(@PathVariable String email) {
        memberService.validateEmail(email);
        memberService.createEmailToken(email);
    }

    //이메일 토큰 확인
    @PostMapping("/join/check-email")
    public ResponseEntity<?> checkEmailToken(@RequestBody EmailAuthRequestDto emailAuthRequestDto) {
        return memberService.checkEmailToken(emailAuthRequestDto);
    }

}
