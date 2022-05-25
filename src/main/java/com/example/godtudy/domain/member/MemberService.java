package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Subject;
import com.example.godtudy.domain.member.entity.SubjectEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final PasswordEncoder passwordEncoder;

    public Member joinMember(MemberJoinForm memberJoinForm) {
        memberJoinForm.setPassword(passwordEncoder.encode(memberJoinForm.getPassword()));
        Member newMember = memberJoinForm.toEntity();

        return memberRepository.save(newMember);
    }

    private void checkSubject(String title){
        EnumUtils.findEnumInsensitiveCase(SubjectEnum.class, title);
    }

    public void addSubject(Member member, MemberJoinForm memberJoinForm) {
        if (memberJoinForm.getSubject().isEmpty()) {
            throw new IllegalArgumentException("필수 입력사항입니다.");
        }
        for (String title: memberJoinForm.getSubject()) {
            checkSubject(title);
            Subject subject = Subject.builder().title(title).member(member).build();
            subjectRepository.save(subject);
        }
    }
}
