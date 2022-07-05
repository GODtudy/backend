package com.example.godtudy.domain.member.service;

import com.example.godtudy.domain.member.dto.request.ProfileRequestDto;
import com.example.godtudy.domain.member.dto.response.ProfileResponseDto;
import com.example.godtudy.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
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
    }
}
