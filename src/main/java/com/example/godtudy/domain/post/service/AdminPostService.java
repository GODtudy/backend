package com.example.godtudy.domain.post.service;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import com.example.godtudy.domain.post.dto.request.PostUpdateRequestDto;
import com.example.godtudy.domain.post.entity.AdminPost;
import com.example.godtudy.domain.post.repository.AdminPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminPostService {

    private final AdminPostRepository adminPostRepository;
    private final MemberRepository memberRepository;

    private void checkIfAdmin(Member member) {
        Member checkMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        if (!checkMember.getRole().equals(Role.ADMIN) ) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    public ResponseEntity<?> createAdminPost(Member member, String post, PostSaveRequestDto postSaveRequestDto) {
        checkIfAdmin(member); //관리자인지 확인

        AdminPost adminPost = postSaveRequestDto.toNoticeEntity();
        adminPost.setAuthor(member); // 현재 맴버 매핑
        adminPost.setAdminPostEnum(post); // 현재 게시판 작성
        member.addAdminPost(adminPost);

        adminPostRepository.save(adminPost);

        return new ResponseEntity<>("Notice Create", HttpStatus.OK);
    }

    public ResponseEntity<?> updateAdminPost(Member member, PostUpdateRequestDto postUpdateRequestDto) {
        checkIfAdmin(member);


        return new ResponseEntity<>("Notice Update", HttpStatus.OK);
    }
}
