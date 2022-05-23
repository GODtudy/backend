package com.example.godtudy.domain.validator;

import com.example.godtudy.domain.member.MemberRepository;
import com.example.godtudy.domain.member.dto.StudentJoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudentJoinFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(StudentJoinForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudentJoinForm studentJoinForm = (StudentJoinForm) target;

        if (memberRepository.existsByUsername(studentJoinForm.getUsername())) {
            errors.rejectValue("username", "invalid username", new Object[]{studentJoinForm.getUsername()}, "이미 사용중인 아이디 입니다.");
        }
        if (memberRepository.existsByEmail(studentJoinForm.getEmail())) {
            errors.rejectValue("email", "invalid email", new Object[]{studentJoinForm.getEmail()}, "이미 인증된 이메일 입니다.");
        }
        if (memberRepository.existsByNickname(studentJoinForm.getNickname())) {
            errors.rejectValue("nickname", "invalid nickname", new Object[]{studentJoinForm.getNickname()}, "이미 인증된 이메일 입니다.");
        }

    }
}
