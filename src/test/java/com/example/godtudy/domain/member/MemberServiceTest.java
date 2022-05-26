package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void beforeEach(){
        MemberJoinForm memberJoinForm = MemberJoinForm.builder()
                .username("swchoi1997")
                .password("tkddnjs4371@")
                .name("최상원")
                .email("swchoi1997@naver.com")
                .nickname("숲속의냉면")
                .year("1997").month("02").day("12")
                .build();
        memberService.joinMember(memberJoinForm);
    }

    @AfterEach
    void afterEach(){
        memberRepository.deleteAll();
    }


//    @DisplayName("학생 회원가입 - 정상")
    @Test
    public void joinStudent() throws Exception{
        //given
        MemberJoinForm newMember = MemberJoinForm.builder()
                .username("test1")
                .password("tkddnjs4371@")
                .name("최상원")
                .email("test1@naver.com")
                .nickname("test1")
                .year("1997").month("02").day("12")
                .build();
        //when
        memberService.joinMember(newMember);

        //then
        Member member = memberRepository.findByUsername(newMember.getUsername()).orElseThrow(() -> new Exception("유저가 없습니다."));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getUsername()).isEqualTo(newMember.getUsername());
        assertThat(member.getName()).isEqualTo(newMember.getName());
        assertThat(member.getEmail()).isEqualTo(newMember.getEmail());
        assertThat(member.getNickname()).isEqualTo(newMember.getNickname());
        assertThat(passwordEncoder.matches(member.getPassword(), newMember.getPassword()));
    }

}