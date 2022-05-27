package com.example.godtudy.domain.member.service;

import com.example.godtudy.domain.member.dto.request.*;
import com.example.godtudy.domain.member.dto.response.MemberLoginResponseDto;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.repository.SubjectRepository;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Subject;
import com.example.godtudy.domain.member.entity.SubjectEnum;
import com.example.godtudy.global.advice.exception.LoginFailureException;
import com.example.godtudy.global.advice.exception.MemberEmailAlreadyExistsException;
import com.example.godtudy.global.advice.exception.MemberNicknameAlreadyExistsException;
import com.example.godtudy.global.advice.exception.MemberUsernameAlreadyExistsException;
import com.example.godtudy.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /*  로그인  */
    public MemberLoginResponseDto loginMember(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberRepository.findByUsername(memberLoginRequestDto.getUsername())
                .orElseThrow(LoginFailureException::new);

        if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }

        return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(memberLoginRequestDto.getUsername()));
    }

    /*  회원가입  */
    public Member joinMember(MemberJoinForm memberJoinForm, String role) {

        memberJoinForm.setPassword(passwordEncoder.encode(memberJoinForm.getPassword()));
        memberJoinForm.setRole(Role.valueOf(role.toUpperCase()));
        Member newMember = memberJoinForm.toEntity();

        return memberRepository.save(newMember);
    }

    /*   과목 명 받아서 저장   */
    public void addSubject(Member member, MemberJoinForm memberJoinForm) {
        if (memberJoinForm.getSubject().isEmpty()) {
            throw new IllegalArgumentException("입력하지 않은 부분이 있습니다. 확인해 주세요.");
        }
        for (SubjectEnum title: memberJoinForm.getSubject()) {
            Subject subject = Subject.builder().title(title).member(member).build();
            subjectRepository.save(subject);
        }
    }

    //아이디 중복확인
    @Transactional(readOnly = true)
    public void usernameCheckDuplication(UsernameRequestDto usernameRequestDto) {
        memberRepository.findByUsername(usernameRequestDto.getUsername())
                .ifPresent(e -> {
                    throw new MemberUsernameAlreadyExistsException("이미 사용중인 아이디 입니다.");
                });
    }
    // 인증된 이메일 Or 이메일 중복확인
    @Transactional(readOnly = true)
    public void emailCheckDuplication(EmailRequestDto emailRequestDto) {
        memberRepository.findByEmail(emailRequestDto.getEmail())
                .ifPresent(e -> {
                    throw new MemberEmailAlreadyExistsException("이미 인증된 이메일 입니다.");
                });
    }

    //닉네임 중복확인
    @Transactional(readOnly = true)
    public void nicknameCheckDuplication(NicknameRequestDto nicknameRequestDto) {
        memberRepository.findByNickname(nicknameRequestDto.getNickname())
                .ifPresent(e -> {
                    throw new MemberNicknameAlreadyExistsException("이미 사용중인 닉네임 입니다.");
                });
    }
}
