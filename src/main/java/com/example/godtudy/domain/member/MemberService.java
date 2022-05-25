package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member joinMember(MemberJoinForm memberJoinForm) {
        memberJoinForm.setPassword(passwordEncoder.encode(memberJoinForm.getPassword()));
        Member newMember = memberJoinForm.toEntity();

        return memberRepository.save(newMember);
    }

}
