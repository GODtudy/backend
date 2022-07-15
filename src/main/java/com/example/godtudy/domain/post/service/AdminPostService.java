package com.example.godtudy.domain.post.service;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import com.example.godtudy.domain.post.entity.AdminPost;
import com.example.godtudy.domain.post.repository.AdminPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminPostService {

    private final AdminPostRepository adminPostRepository;
    private final MemberRepository memberRepository;

    private void checkIfAdmin(Member member) {
        if (!memberRepository.findByUsername(member.getRole().toString()).isPresent()) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }

    }

    public ResponseEntity<?> createAdminPost(Member member, PostSaveRequestDto postSaveRequestDto) {
        checkIfAdmin(member);

        AdminPost adminPost = postSaveRequestDto.toNoticeEntity();
        AdminPost.createMemberNotice(member);
        adminPostRepository.save(adminPost);

        return new ResponseEntity<>("Notice Save", HttpStatus.OK);
    }
}
