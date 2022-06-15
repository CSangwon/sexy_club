package com.example.chattest.modules.member.dto.request;

import com.example.chattest.global.validator.ValidationGroups;
import com.example.chattest.modules.member.entity.Member;
import com.example.chattest.modules.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinRequestDto {

    @NotBlank(message = "이름은 필수 입력사항입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "[가-힣]{2,4}",
            message = "올바른 이름을 입력해주세요"
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String name;

    @NotBlank(message = "이메일은 필수 입력사항입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
            message = "올바르지 않은 이메일 형식입니다."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다..", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[0-9a-zA-Z])(?=.*[~!@#$%^&*()=+])[0-9a-zA-Z\\d~!@#$%^&*()=+]{8,20}",
            message = "비밀번호는 영문과 숫자 특수문자 조합으로 8 ~ 20자리로 설정해주세요."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String passwordConfirm;

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(Role.MEMBER)
                .build();
    }
}
