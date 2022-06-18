package com.example.chattest.global;

import com.example.chattest.modules.member.entity.Member;
import com.example.chattest.modules.member.entity.Role;
import com.example.chattest.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final MemberInitService memberInitService;

    @PostConstruct
    public void init() {
        memberInitService.insertMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class MemberInitService {
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;

        public void insertMember() {
            Member member = Member.builder()
                    .name("최상워")
                    .email("swchoi1997@naver.com")
                    .password(passwordEncoder.encode("tkddnjs8528##"))
                    .emailVerified(true)
                    .emailCheckTokenGeneratedAt(LocalDate.now())
                    .role(Role.MEMBER)
                    .build();
            memberRepository.save(member);
        }
    }

}
