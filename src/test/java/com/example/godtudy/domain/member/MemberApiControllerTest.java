package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {


    @Autowired
    MockMvc mockMvc;
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

//    @DisplayName("학생 회원가입 - 아이디 중복")
    @Test
    public void joinStudent_error_username() throws Exception{
        mockMvc.perform(post("/api/member/join/student")
                        .param("username", "swchoi1997")
                        .param("password", "tkddnjs4371@")
                        .param("name", "최상원")
                        .param("email", "swchoi19972@naver.com")
                        .param("nickname", "test1")
                        .param("year", "1997")
                        .param("month", "02")
                        .param("day", "12"))
                .andExpect(status().isBadRequest());

    }

}
