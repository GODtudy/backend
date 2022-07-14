package com.example.godtudy.domain.post.service;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface PostService {

    ResponseEntity<?> createPost(Member member, PostSaveRequestDto postSaveRequestDto);
}
