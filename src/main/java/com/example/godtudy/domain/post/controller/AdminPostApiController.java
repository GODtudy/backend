package com.example.godtudy.domain.post.controller;

import com.example.godtudy.domain.member.entity.CurrentMember;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import com.example.godtudy.domain.post.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/admin/notice/")
@RequiredArgsConstructor
public class AdminPostApiController {

    private final AdminPostService adminPostService;

    public ResponseEntity<?> createNoticeEvent(@CurrentMember Member member, @RequestBody PostSaveRequestDto postSaveRequestDto) {
        return adminPostService.createAdminPost(member, postSaveRequestDto);
    }



}
