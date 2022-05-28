package com.example.godtudy.domain.member.controller;

import com.example.godtudy.domain.member.dto.request.*;
import com.example.godtudy.domain.member.dto.response.MemberLoginResponseDto;
import com.example.godtudy.domain.member.service.MemberService;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.member.validator.MemberJoinFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final MemberJoinFormValidator memberJoinFormValidator;

    @ExceptionHandler({
            IllegalStateException.class
    })

    @InitBinder("memberJoinForm")
    public void initBinder1(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberJoinFormValidator);
    }

//    /*     로그인     */
//    @PostMapping("/login")
//    public ResponseEntity<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
//        MemberLoginResponseDto memberLoginResponseDto = memberService.loginMember(memberLoginRequestDto);
//
//        return new ResponseEntity<>(memberLoginResponseDto, HttpStatus.OK);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken_login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return memberService.createAuthenticationToken(memberLoginRequestDto);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<?> reissueAuthenticationToken(@RequestBody TokenRequestDto tokenRequestDto) {
        return memberService.reissueAuthenticationToken(tokenRequestDto);
    }

    @PostMapping("/logout")
    public void deleteAuthenticationToken(@RequestBody MemberLogoutRequestDto memberLogoutRequestDto) {
        memberService.deleteAuthenticationToken(memberLogoutRequestDto);
    }

    /*     회원가입     */
    @PostMapping("/join/{role}")
    public ResponseEntity<?> joinMember(@Valid @RequestBody MemberJoinForm memberJoinForm, @PathVariable String role, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        memberJoinForm.setRole(Role.STUDENT);
        Member member = memberService.joinMember(memberJoinForm, role);
        memberService.addSubject(member, memberJoinForm);

        return new ResponseEntity<>("Join Success", HttpStatus.OK);
    }

    /*     아이디 중복 확인     */
    @PostMapping("/join/student/username")
    public ResponseEntity<?> usernameCheck(@Valid @RequestBody UsernameRequestDto usernameRequestDto, Errors errors) {
        memberService.usernameCheckDuplication(usernameRequestDto);
        if (errors.hasErrors()) {
            return new ResponseEntity<>("Invalid Username", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Check Username", HttpStatus.OK);
    }

    /*     이메일 중복 확인     */
    @PostMapping("/join/student/email")
    public ResponseEntity<?> emailCheck(@Valid @RequestBody EmailRequestDto emailRequestDto, Errors errors) {
        memberService.emailCheckDuplication(emailRequestDto);
        if (errors.hasErrors()) {
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Check Username", HttpStatus.OK);
    }

    /*     닉네임 중복 확인     */
    @PostMapping("/join/student/nickname")
    public ResponseEntity<?> nicknameCheck(@Valid @RequestBody NicknameRequestDto nicknameRequestDto, Errors errors) {
        memberService.nicknameCheckDuplication(nicknameRequestDto);
        if (errors.hasErrors()) {
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Check Username", HttpStatus.OK);
    }

//    {
//        "username": "test1",
//            "password": "tkddnjs8528##",
//            "name" : "test1",
//            "email" : "test1@nvaer.com",
//            "nickname": "test1",
//            "year": "1997",
//            "month": "02",
//            "day": "12",
//            "subject": [
//        "ENGLISH", "BIOLOGY"
//    ]
//    }

}
