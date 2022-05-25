package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.MemberJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.validator.StudentJoinFormValidator;
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

    private final StudentJoinFormValidator studentJoinFormValidator;

    @InitBinder("studentJoinForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studentJoinFormValidator);
    }

    /*    학생 회원가입     */
    @PostMapping("/join/student")
    public ResponseEntity<?> joinMemberStudent(@Valid @RequestBody MemberJoinForm memberJoinForm, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        memberJoinForm.setRole(Role.STUDENT);
        Member member = memberService.joinMember(memberJoinForm);

        memberService.addSubject(member, memberJoinForm);

        return new ResponseEntity<>("Join Success", HttpStatus.OK);
    }

    /*    선생님 회원가입     */
    @PostMapping("/join/teacher")
    public ResponseEntity<?> joinMemberTeacher(@Valid @RequestBody MemberJoinForm memberJoinForm, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        memberJoinForm.setRole(Role.TEACHER);
        memberService.joinMember(memberJoinForm);
        return new ResponseEntity<>("Join Success", HttpStatus.OK);
    }

}
