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


    public ResponseEntity<?> createAdminPost(Member member, String post, PostSaveRequestDto postSaveRequestDto) {
        checkIfAdmin(member); //관리자인지 확인

        AdminPost adminPost = postSaveRequestDto.toNoticeEntity();
        adminPost.setAuthor(member); // 현재 맴버 매핑
        adminPost.setAdminPostEnum(post); // 현재 게시판 작성
        member.addAdminPost(adminPost);

        adminPostRepository.save(adminPost);

        return new ResponseEntity<>("Notice Create", HttpStatus.OK);
    }

    public ResponseEntity<?> updateAdminPost(Member member,Long id, PostUpdateRequestDto postUpdateRequestDto) {
        AdminPost adminPost = adminPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        checkIfAdmin(member);
        checkAuthor(member, adminPost);

        adminPost.updateAdminPost(postUpdateRequestDto);

        return new ResponseEntity<>("Notice Update", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAdminPost(Member member, Long id) {
        AdminPost adminPost = adminPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        checkIfAdmin(member);
        checkAuthor(member, adminPost);

        adminPostRepository.delete(adminPost);

        return new ResponseEntity<>("Notice Delete", HttpStatus.OK);
    }

    //TODO 페이징해서 게시글 가져오는거 구현해야하고 파일 업로드하는거랑 댓글 기능까지 작성해야함, 테스트코드도 작성해야함

    /**
     * 관리자인지 확인
     */
    private void checkIfAdmin(Member member) {
        Member checkMember = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        if (!checkMember.getRole().equals(Role.ADMIN) ) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    /**
     * 이 게시글의 작성자인지 확인
     */
    private void checkAuthor(Member member, AdminPost adminPost) {
        if (!adminPost.getMember().getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }
}
