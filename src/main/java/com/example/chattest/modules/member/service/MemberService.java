package com.example.chattest.modules.member.service;

import com.example.chattest.global.advice.exception.EmailAuthTokenNotFountException;
import com.example.chattest.global.jwt.JwtTokenProvider;
import com.example.chattest.global.jwt.member.MemberDetails;
import com.example.chattest.global.jwt.member.MemberDetailsService;
import com.example.chattest.modules.mail.dto.EmailMessage;
import com.example.chattest.modules.mail.entity.EmailAuth;
import com.example.chattest.modules.mail.repository.EmailAuthRepository;
import com.example.chattest.modules.mail.service.EmailService;
import com.example.chattest.modules.member.dto.request.*;
import com.example.chattest.modules.member.dto.response.JwtTokenResponseDto;
import com.example.chattest.modules.member.entity.JwtRefreshToken;
import com.example.chattest.modules.member.entity.Member;
import com.example.chattest.modules.member.repository.JwtRefreshTokenRepository;
import com.example.chattest.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final EmailAuthRepository emailAuthRepository;

    private final TemplateEngine templateEngine;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;
    private final AuthenticationManager authenticationManager;

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

        //이메일 토큰 생성 후
        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .authToken(emailToken.substring(0, 6))
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

    public ResponseEntity<?> login(MemberLoginRequestDto memberLoginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword())
        );

        final String accessToken = jwtTokenProvider.createAccessToken(memberLoginRequestDto.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken();
        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
                .email(memberLoginRequestDto.getEmail())
                .refreshToken(refreshToken)
                .build();

        jwtRefreshTokenRepository.save(jwtRefreshToken);

        final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(memberLoginRequestDto.getEmail());

        return ResponseEntity.ok(new MemberLoginRequestDto(memberDetails.getId(), memberLoginRequestDto.getEmail()), accessToken, refreshToken);
    }

    public ResponseEntity<?> reissueAuthenticationToken(TokenRequestDto tokenRequestDto) {
        // 사용자로부터 받은 Refresh Token 유효성 검사
        // Refresh Token 마저 만료되면 다시 로그인
        if (jwtTokenProvider.isTokenExpired(tokenRequestDto.getRefreshToken())
                || !jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("잘못된 요청입니다. 다시 로그인해주세요.");
        }

        // Access Token 에 기술된 사용자 이름 가져오기
        String username = jwtTokenProvider.getUsernameFromToken(tokenRequestDto.getAccessToken());
        JwtRefreshToken byUsername = jwtRefreshTokenRepository.findByUsername(username);

        // 데이터베이스에 저장된 Refresh Token 과 비교
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findById(byUsername.getId()).orElseThrow(
                () -> new IllegalArgumentException("잘못된 요청입니다. 다시 로그인해주세요.")
        );
        if (!jwtRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token 정보가 일치하지 않습니다.");
        }

        // 새로운 Access Token 발급
        final String accessToken = jwtTokenProvider.createAccessToken(username);

        return ResponseEntity.ok(new JwtTokenResponseDto(accessToken));
    }

    // 로그아웃 토근제거
    public void logout(MemberLogoutRequestDto memberLogoutRequestDto) {
        JwtRefreshToken refreshToken = jwtRefreshTokenRepository.findByUsername(memberLogoutRequestDto.getEmail());
        jwtRefreshTokenRepository.deleteById(refreshToken.getId());
    }
}
