package com.example.godtudy.domain.service;

import com.example.godtudy.WithMember;
import com.example.godtudy.domain.member.dto.response.ProfileResponseDto;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ProfileServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileService profileService;

    @WithMember("swchoi1997")
    @DisplayName("WithMember 테스트")
    @Test
    public void withMemberTest() throws Exception{
        Optional<Member> member = memberRepository.findByUsername("swchoi1997");
        assertThat(member.isPresent()).isTrue();
    }

    @WithMember("swchoi1997")
    @DisplayName("개인정보 가져오기")
    @Test
    public void getProfileTest() throws Exception{
        //given
        Optional<Member> member = memberRepository.findByUsername("swchoi1997");

        //when
        ProfileResponseDto memberProfile = profileService.getProfile(member.get());

        //then
        assertThat(memberProfile.getUsername()).isEqualTo("swchoi1997");
        assertThat(memberProfile.getEmail()).isEqualTo("swchoi1997@naver.com");
        assertThat(memberProfile.getNickname()).isEqualTo("숲속의냉면");
        assertThat(memberProfile.getBio()).isNull();
        assertThat(memberProfile.getProfileImageUrl()).isNull();
    }





}