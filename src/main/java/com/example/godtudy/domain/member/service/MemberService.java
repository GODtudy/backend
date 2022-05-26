package com.example.godtudy.domain.member.service;

import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.repository.SubjectRepository;
import com.example.godtudy.domain.member.dto.EmailRequestDto;
import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.dto.NicknameRequestDto;
import com.example.godtudy.domain.member.dto.UsernameRequestDto;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Subject;
import com.example.godtudy.domain.member.entity.SubjectEnum;
import com.example.godtudy.global.advice.exception.MemberUsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;


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

    /*   과목 명 받아서 저장   */
    public void addSubject(Member member, MemberJoinForm memberJoinForm) {
        if (memberJoinForm.getSubject().isEmpty()) {
            throw new IllegalArgumentException("입력하지 않은 부분이 있습니다. 확인해 주세요.");
        }
        for (String title: memberJoinForm.getSubject()) {
            checkSubject(title);
            Subject subject = Subject.builder().title(title).member(member).build();
            subjectRepository.save(subject);
        }
    }
    @Transactional(readOnly = true)
    public void emailCheckDuplication(EmailRequestDto emailRequestDto) {
        memberRepository.findByEmail(emailRequestDto.getEmail())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("이미 인증된 이메일 입니다.");
                });
    }

    @Transactional(readOnly = true)
    public void usernameCheckDuplication(UsernameRequestDto usernameRequestDto) {
        memberRepository.findByUsername(usernameRequestDto.getUsername())
                .ifPresent(e -> {
                    throw new MemberUsernameAlreadyExistsException("이미 사용중인 아이디 입니다.");
                });
    }

    public void nicknameCheckDuplication(NicknameRequestDto nicknameRequestDto) {
        memberRepository.findByNickname(nicknameRequestDto.getNickname())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("이미 사용중인 닉네임 입니다.");
                });
    }
}
