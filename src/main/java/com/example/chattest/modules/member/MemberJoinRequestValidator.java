package com.example.chattest.modules.member;

import com.example.chattest.modules.member.dto.request.MemberJoinRequestDto;
import com.example.chattest.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberJoinRequestValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberJoinRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberJoinRequestDto memberJoinRequestDto = (MemberJoinRequestDto) target;

        if (memberRepository.existsByEmail(memberJoinRequestDto.getEmail())) {
            errors.rejectValue("email", "invalid email", new Object[]{memberJoinRequestDto.getEmail()}, "이미 인증된 이메일 입니다.");
        }

        if (!memberJoinRequestDto.getPassword().equals(memberJoinRequestDto.getPasswordConfirm())) {
            errors.rejectValue("password", "invalid password", new Object[]{memberJoinRequestDto.getPasswordConfirm()}, "비밀번호를 확인해 주세요");
        }
    }
}
