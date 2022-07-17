package com.example.godtudy.domain.post.controller;

import com.example.godtudy.domain.member.entity.CurrentMember;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import com.example.godtudy.domain.post.dto.request.PostUpdateRequestDto;
import com.example.godtudy.domain.post.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPostApiController {

    private final AdminPostService adminPostService;

    @PostMapping("{post}/new")
    public ResponseEntity<?> createNoticeEvent(@PathVariable String post ,@CurrentMember Member member,
                                               @RequestBody PostSaveRequestDto postSaveRequestDto) {
        return adminPostService.createAdminPost(member, post, postSaveRequestDto);
    }

    @PostMapping("/{post}/{id}")
    public ResponseEntity<?> updateNoticeEvent(@PathVariable String post, @PathVariable String id, @CurrentMember Member member,
                                               @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        return adminPostService.updateAdminPost(member, postUpdateRequestDto);
    }



}
