package com.example.godtudy.domain.member.dto;

import com.example.godtudy.global.validation.ValidationGroups;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Builder
public class StudentJoinForm {


    @NotBlank(message = "아이디는 필수 입력사항입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "[a-zA-Z0-9]{2,15}",
            message = "아이디는 영문, 숫자 조합만 가능하며 2 ~ 15 자리 까지 가능합니다."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다..", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[0-9a-zA-Z])(?=.*[~!@#$%^&*()=+])[0-9a-zA-Z\\d~!@#$%^&*()=+]{8,20}",
            message = "비밀번호는 영문과 숫자 특수문자 조합으로 8 ~ 20자리로 설정해주세요."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String password;

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

    @NotBlank(message = "닉네임을 입력해주세요", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]{4,10}",
            message = "닉네임은 한글, 영어, 숫자만 4 ~10자리로 입력 가능합니다"
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String nickname;

    private Date birthday;

    private Role role;

    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .role(Role.STUDENT)
                .build();
    }
}
