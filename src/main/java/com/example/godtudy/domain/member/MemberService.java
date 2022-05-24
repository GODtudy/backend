package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.StudentJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    //TODO 만약에 teacher, student, parents별로 회원가입을 나눠야한다면?!? service를 interface로 만들어 줄 필요가 있어보임
    //TODO 그리고 지금 생각에는 JoinForm 이 상당부분 겹치게 될 것 같은데, 선생님, 학부모한테서만 추가정보 들고온다면 공통의 Form 하나와 각 정보 하나를 나눠도 되지 않을까?
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member joinMember(StudentJoinForm studentJoinForm) {
        studentJoinForm.setPassword(passwordEncoder.encode(studentJoinForm.getPassword()));
        Member newMember = studentJoinForm.toEntity();

        return memberRepository.save(newMember);
    }

}
