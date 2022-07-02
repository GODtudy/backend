package com.example.godtudy.domain.member.controller;

import com.example.godtudy.domain.member.dto.response.ProfileResponseDto;
import com.example.godtudy.domain.member.entity.CurrentMember;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApiController {

    private final ProfileService profileService;

    @GetMapping("{profileId}")
    public ResponseEntity<ProfileResponseDto> profileInquiry(@CurrentMember Member member, @PathVariable String profileId) {
        ProfileResponseDto profile = profileService.getProfile(member);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

}
