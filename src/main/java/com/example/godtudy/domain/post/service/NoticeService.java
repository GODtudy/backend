package com.example.godtudy.domain.post.service;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import com.example.godtudy.domain.post.entity.Notice;
import com.example.godtudy.domain.post.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService implements PostService{

    private final NoticeRepository noticeRepository;

    @Override
    public ResponseEntity<?> createPost(Member member, PostSaveRequestDto postSaveRequestDto) {
        Notice notice = postSaveRequestDto.toNoticeEntity();
        Notice.createMemberNotice(member);
        noticeRepository.save(notice);

        return new ResponseEntity<>("Notice Save", HttpStatus.OK);
    }
}
