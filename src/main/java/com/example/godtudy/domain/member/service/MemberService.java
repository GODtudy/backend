package com.example.godtudy.domain.member.service;

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
import com.example.godtudy.global.security.jwt.JwtTokenProvider;
import com.example.godtudy.global.security.member.MemberDetails;
import com.example.godtudy.global.security.member.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    private final JavaMailSender javaMailSender;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;



    public ResponseEntity<?> login(MemberLoginRequestDto memberLoginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getUsername(), memberLoginRequestDto.getPassword()));

        final String accessToken = jwtTokenProvider.createAccessToken(memberLoginRequestDto.getUsername());
        final String refreshToken = jwtTokenProvider.createRefreshToken();

        // 데이터베이스에 Refresh Token 저장
        jwtRefreshTokenRepository.save(new JwtRefreshToken(memberLoginRequestDto.getUsername(), refreshToken));

        final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(memberLoginRequestDto.getUsername());

        return ResponseEntity.ok(new MemberLoginResponseDto(memberDetails.getId(), memberLoginRequestDto.getUsername(), accessToken, refreshToken));
    }

    public ResponseEntity<?> reissueAuthenticationToken(TokenRequestDto tokenRequestDto) {
        // 사용자로부터 받은 Refresh Token 유효성 검사
        // Refresh Token 마저 만료되면 다시 로그인
        if (jwtTokenProvider.isTokenExpired(tokenRequestDto.getRefreshToken())
                || !jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("잘못된 요청입니다. 다시 로그인해주세요.");
        }

        // Access Token 에 기술된 사용자 이름 가져오기
        String username = jwtTokenProvider.getUsernameFromToken(tokenRequestDto.getAccessToken());

        // 데이터베이스에 저장된 Refresh Token 과 비교
        JwtRefreshToken jwtRefreshToken = jwtRefreshTokenRepository.findById(username).orElseThrow(
                () -> new IllegalArgumentException("잘못된 요청입니다. 다시 로그인해주세요.")
        );
        if (!jwtRefreshToken.getRefreshToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token 정보가 일치하지 않습니다.");
        }

        // 새로운 Access Token 발급
        final String accessToken = jwtTokenProvider.createAccessToken(username);

        return ResponseEntity.ok(new JwtTokenResponseDto(accessToken));
    }

    // 로그아웃 토근제거
    public void logout(MemberLogoutRequestDto memberLogoutRequestDto) {
        jwtRefreshTokenRepository.deleteById(memberLogoutRequestDto.getUsername());
    }

    /*  회원가입  */
    public Member initJoinMember(MemberJoinForm memberJoinForm, String role) {
        String tmpRole = "TMP_" + role.toUpperCase();

        memberJoinForm.setPassword(passwordEncoder.encode(memberJoinForm.getPassword()));
        memberJoinForm.setRole(Role.valueOf(tmpRole));
        Member newMember = memberJoinForm.toEntity();

        //이메일 인증 토큰 값 생성
        newMember.generateEmailCheckToken();

        //이메일 보내기
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newMember.getEmail());
        mailMessage.setSubject("GODtudy, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newMember.getEmailCheckToken() + "&email=" + newMember.getEmail());
        javaMailSender.send(mailMessage);

        return memberRepository.save(newMember);
    }

    /*   과목 명 받아서 저장   */
    public void addSubject(Member member, MemberJoinForm memberJoinForm) {
        if (memberJoinForm.getSubject().isEmpty()) {
            throw new IllegalArgumentException("입력하지 않은 부분이 있습니다. 확인해 주세요.");
        }
        for (SubjectEnum title: memberJoinForm.getSubject()) {
            Subject subject = Subject.builder().title(title).member(member).build();
            subjectRepository.save(subject);
        }
    }

    //아이디 중복확인
    @Transactional(readOnly = true)
    public void usernameCheckDuplication(UsernameRequestDto usernameRequestDto) {
        memberRepository.findByUsername(usernameRequestDto.getUsername())
                .ifPresent(e -> {
                    throw new MemberUsernameAlreadyExistsException("이미 사용중인 아이디 입니다.");
                });
    }
    // 인증된 이메일 Or 이메일 중복확인
    @Transactional(readOnly = true)
    public void emailCheckDuplication(EmailRequestDto emailRequestDto) {
        memberRepository.findByEmail(emailRequestDto.getEmail())
                .ifPresent(e -> {
                    throw new MemberEmailAlreadyExistsException("이미 인증된 이메일 입니다.");
                });
    }

    //닉네임 중복확인
    @Transactional(readOnly = true)
    public void nicknameCheckDuplication(NicknameRequestDto nicknameRequestDto) {
        memberRepository.findByNickname(nicknameRequestDto.getNickname())
                .ifPresent(e -> {
                    throw new MemberNicknameAlreadyExistsException("이미 사용중인 닉네임 입니다.");
                });
    }
}
