package com.example.chattest.modules.member.service;

import com.example.chattest.global.advice.exception.EmailAuthTokenNotFountException;
import com.example.chattest.global.config.AppProperties;
import com.example.chattest.modules.mail.dto.EmailMessage;
import com.example.chattest.modules.mail.entity.EmailAuth;
import com.example.chattest.modules.mail.repository.EmailAuthCustomRepository;
import com.example.chattest.modules.mail.repository.EmailAuthRepository;
import com.example.chattest.modules.mail.service.EmailService;
import com.example.chattest.modules.member.Member;
import com.example.chattest.modules.member.dto.request.EmailAuthRequestDto;
import com.example.chattest.modules.member.dto.request.MemberJoinRequestDto;
import com.example.chattest.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;

    private final TemplateEngine templateEngine;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public ResponseEntity<?> joinMember(MemberJoinRequestDto memberJoinRequestDto) {

        //1. 이메일 인증시 저장한 이메일을 찾아오고
        EmailAuth emailAuth = emailAuthRepository.findByEmail(memberJoinRequestDto.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        //2. 이메일 인증이 되었는지 확인
        if (!emailAuth.getExpired()) {
            return new ResponseEntity<>("인증이 완료 되지 않았습니다.", HttpStatus.BAD_REQUEST);
        }
        //3. 이메일 인증이 되었다면 회원가입 진행
        memberJoinRequestDto.setPassword(passwordEncoder.encode(memberJoinRequestDto.getPassword()));
        Member newMember = memberJoinRequestDto.toEntity();

        memberRepository.save(newMember);
        return new ResponseEntity<>("Join Success", HttpStatus.OK);
    }

    //이메일 인증 토큰 생성
    public void createEmailToken(String email) {
        String emailToken = UUID.randomUUID().toString();
        emailToken.substring(0, emailToken.indexOf("-"));
        //이메일 토큰 생성 후
        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .authToken(emailToken)
                .expired(false)
                .build();
        emailAuth.setTokenExpireTime();
        log.info(emailAuth.toString());

        //저장
        emailAuthRepository.save(emailAuth);

        //이메일 보냄
        sendJoinEmailToken(emailAuth);
    }

    //이메일 중복확인
    public void validateEmail(String email) {
        if (emailAuthRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 인증된 이메일 입니다.");
        }
    }

    //이메일 보내기
    public void sendJoinEmailToken(EmailAuth emailAuth) {
        Context context = new Context();
        context.setVariable("token", emailAuth.getAuthToken());
        context.setVariable("email", emailAuth.getEmail());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "이메일 인증을 완료하기 위해서 아래 문자를 입력해 주세요");

        String message = templateEngine.process("mail/check-emailToken", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailAuth.getEmail())
                .subject("회원가입 이메일 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    //이메일 토큰 인증
    public ResponseEntity<?> checkEmailToken(EmailAuthRequestDto emailAuthRequestDto) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(
                emailAuthRequestDto.getEmail(), emailAuthRequestDto.getExpireToken(), LocalDateTime.now())
                .orElseThrow(EmailAuthTokenNotFountException::new);

        // 인증 완료 해줌
        emailAuth.setToken();

        return new ResponseEntity<>("Email Check Ok", HttpStatus.OK);
    }
}
