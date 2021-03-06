package com.example.godtudy.domain.member.service;

import com.example.godtudy.domain.mail.EmailMessage;
import com.example.godtudy.domain.mail.EmailService;
import com.example.godtudy.domain.member.dto.request.*;
import com.example.godtudy.domain.member.dto.response.JwtTokenResponseDto;
import com.example.godtudy.domain.member.dto.response.MemberLoginResponseDto;
import com.example.godtudy.domain.member.entity.*;
import com.example.godtudy.domain.member.repository.JwtRefreshTokenRepository;
import com.example.godtudy.domain.member.repository.MemberRepository;
import com.example.godtudy.domain.member.repository.SubjectRepository;
import com.example.godtudy.global.advice.exception.MemberEmailAlreadyExistsException;
import com.example.godtudy.global.advice.exception.MemberNicknameAlreadyExistsException;
import com.example.godtudy.global.advice.exception.MemberUsernameAlreadyExistsException;
import com.example.godtudy.global.config.AppProperties;
import com.example.godtudy.global.security.jwt.JwtTokenProvider;
import com.example.godtudy.global.security.member.MemberDetails;
import com.example.godtudy.global.security.member.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;



    public ResponseEntity<?> login(MemberLoginRequestDto memberLoginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getUsername(), memberLoginRequestDto.getPassword()));

        final String accessToken = jwtTokenProvider.createAccessToken(memberLoginRequestDto.getUsername());
        final String refreshToken = jwtTokenProvider.createRefreshToken();
        JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
                .username(memberLoginRequestDto.getUsername())
                .refreshToken(refreshToken)
                .build();

        // ????????????????????? Refresh Token ??????
        jwtRefreshTokenRepository.save(jwtRefreshToken);

        final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(memberLoginRequestDto.getUsername());

        return ResponseEntity.ok(new MemberLoginResponseDto(memberDetails.getId(), memberLoginRequestDto.getUsername(), accessToken, refreshToken));
    }

    public ResponseEntity<?> reissueAuthenticationToken(TokenRequestDto tokenRequestDto) {
        // ?????????????????? ?????? Refresh Token ????????? ??????
        // Refresh Token ?????? ???????????? ?????? ?????????
        if (jwtTokenProvider.isTokenExpired(tokenRequestDto.getRefreshToken())
                || !jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("????????? ???????????????. ?????? ?????????????????????.");
        }

        // Access Token ??? ????????? ????????? ?????? ????????????
        String username = jwtTokenProvider.getUsernameFromToken(tokenRequestDto.getAccessToken());
        JwtRefreshToken byUsername = jwtRefreshTokenRepository.findByUsername(username);

        // ????????????????????? ????????? Refresh Token ??? ??????
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findById(byUsername.getId()).orElseThrow(
                () -> new IllegalArgumentException("????????? ???????????????. ?????? ?????????????????????.")
        );
        if (!jwtRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token ????????? ???????????? ????????????.");
        }

        // ????????? Access Token ??????
        final String accessToken = jwtTokenProvider.createAccessToken(username);

        return ResponseEntity.ok(new JwtTokenResponseDto(accessToken));
    }

    // ???????????? ????????????
    public void logout(MemberLogoutRequestDto memberLogoutRequestDto) {
        JwtRefreshToken refreshToken = jwtRefreshTokenRepository.findByUsername(memberLogoutRequestDto.getUsername());
        jwtRefreshTokenRepository.deleteById(refreshToken.getId());
    }

    /*  ????????????  */
    public Member initJoinMember(MemberJoinForm memberJoinForm, String role) {
        String tmpRole = "TMP_" + role.toUpperCase();

        memberJoinForm.setPassword(passwordEncoder.encode(memberJoinForm.getPassword()));
        memberJoinForm.setRole(Role.valueOf(tmpRole));
        Member newMember = memberJoinForm.toEntity();

        //????????? ?????? ?????? ??? ??????
        newMember.generateEmailCheckToken();

        sendJoinConfirmEmail(newMember);
        return memberRepository.save(newMember);
    }

    /* ????????? ????????? */
    public void sendJoinConfirmEmail(Member member) {
        Context context = new Context();
        context.setVariable("link", "/api/member/checkEmailToken/" + member.getEmailCheckToken() + "/" + member.getEmail());
        context.setVariable("username", member.getUsername());
        context.setVariable("linkName", "????????? ????????????");
        context.setVariable("message", "GODtudy ???????????? ??????????????? ?????? ????????? ???????????????!");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .subject("GODtudy, ???????????? ??????")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
        //TODO ?????? host??? ????????? ???????????? ????????? ????????? ??????????????? null?????????
    }



    /*  ???????????? ?????? ?????? ??????   */
    public ResponseEntity<?> checkEmailToken(String token, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("???????????? ?????? ??????????????????."));
        if(member.getEmailVerified() == true) {
            log.info("1======================================================");
            return new ResponseEntity<>("?????? ????????? ???????????????.", HttpStatus.BAD_REQUEST);
        }
        //????????? ????????? ?????????
        if (!member.isValidToken(token)) {
            log.info("2======================================================");
            return new ResponseEntity<>("???????????? ?????? ???????????????.", HttpStatus.BAD_REQUEST);
        }

        return completeJoinMember(member);
    }

    private ResponseEntity<?> completeJoinMember(Member member) {
        member.updateEmailVerified(true, LocalDateTime.now());
        if (member.getRole() == Role.TMP_PARENTS) {
            member.setRole(Role.PARENTS);
        } else if (member.getRole() == Role.TMP_STUDENT) {
            member.setRole(Role.STUDENT);
        } else {
            member.setRole(Role.TEACHER);
        }

        return new ResponseEntity<>("Join Success Final", HttpStatus.OK);
    }



    /*   ?????? ??? ????????? ??????   */

    public void addSubject(Member member, MemberJoinForm memberJoinForm) {
        if (memberJoinForm.getSubject().isEmpty()) {
            throw new IllegalArgumentException("???????????? ?????? ????????? ????????????. ????????? ?????????.");
        }
        for (SubjectEnum title: memberJoinForm.getSubject()) {
            Subject subject = Subject.builder().title(title).member(member).build();
            subjectRepository.save(subject);
        }
    }
    //????????? ????????????
    @Transactional(readOnly = true)
    public void usernameCheckDuplication(UsernameRequestDto usernameRequestDto) {
        memberRepository.findByUsername(usernameRequestDto.getUsername())
                .ifPresent(e -> {
                    throw new MemberUsernameAlreadyExistsException("?????? ???????????? ????????? ?????????.");
                });
    }
    // ????????? ????????? Or ????????? ????????????

    @Transactional(readOnly = true)
    public void emailCheckDuplication(EmailRequestDto emailRequestDto) {
        memberRepository.findByEmail(emailRequestDto.getEmail())
                .ifPresent(e -> {
                    throw new MemberEmailAlreadyExistsException("?????? ????????? ????????? ?????????.");
                });
    }
    //????????? ????????????

    @Transactional(readOnly = true)
    public void nicknameCheckDuplication(NicknameRequestDto nicknameRequestDto) {
        memberRepository.findByNickname(nicknameRequestDto.getNickname())
                .ifPresent(e -> {
                    throw new MemberNicknameAlreadyExistsException("?????? ???????????? ????????? ?????????.");
                });
    }
}
