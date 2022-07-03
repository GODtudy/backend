package com.example.godtudy;

import com.example.godtudy.domain.member.dto.request.MemberJoinForm;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.member.entity.SubjectEnum;
import com.example.godtudy.domain.member.service.MemberService;
import com.example.godtudy.global.security.member.MemberDetails;
import com.example.godtudy.global.security.member.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithMemberSecurityContextFactory implements WithSecurityContextFactory<WithMember> {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDetailsService memberDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithMember withMember) {
        String nickname = withMember.value();

        List<SubjectEnum> subject = new ArrayList<>();
        subject.add(SubjectEnum.BIOLOGY);

        MemberJoinForm memberJoinForm = MemberJoinForm.builder()
                .username(nickname)
                .email(nickname + "@naver.com")
                .password("qwer123!@#")
                .passwordConfirm("qwer123!@#")
                .name("최상원")
                .nickname("숲속의냉면")
                .year("1997")
                .month("02")
                .day("12")
                .role(Role.STUDENT)
                .subject(subject)
                .build();

        memberService.initJoinMember(memberJoinForm, memberJoinForm.getRole().toString());

        UserDetails userDetails = memberDetailsService.loadUserByUsername(nickname);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
