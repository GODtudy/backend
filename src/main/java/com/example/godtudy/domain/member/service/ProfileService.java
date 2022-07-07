package com.example.godtudy.domain.member.service;

import com.example.godtudy.domain.member.dto.request.PasswordUpdateRequestDto;
import com.example.godtudy.domain.member.dto.request.ProfileRequestDto;
import com.example.godtudy.domain.member.dto.response.ProfileResponseDto;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDto getProfile(Member member) {
        return ProfileResponseDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .profileImageUrl(member.getProfileImageUrl())
                .subject(member.getSubject())
                .build();
    }

    public void updateProfile(Member member, ProfileRequestDto profileRequestDto) {
        member.updateProfile(profileRequestDto);
        memberRepository.save(member);
    }

    public void updatePassword(Member member, PasswordUpdateRequestDto passwordUpdateRequestDto) {
        if (!passwordUpdateRequestDto.getNewPassword().equals(passwordUpdateRequestDto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        member.updatePassword(passwordEncoder.encode(passwordUpdateRequestDto.getNewPassword()));
        memberRepository.save(member);
    }
}
