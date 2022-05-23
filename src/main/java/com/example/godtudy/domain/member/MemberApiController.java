package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.StudentJoinForm;
import com.example.godtudy.domain.validator.StudentJoinFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    private final StudentJoinFormValidator studentJoinFormValidator;

    @InitBinder("studentJoinForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studentJoinFormValidator);
    }

    /*    학생 회원가입     */
    @PostMapping("/join/student")
    public ResponseEntity<?> joinMember(@Valid @RequestBody StudentJoinForm studentJoinForm, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        memberService.joinMember(studentJoinForm);
        return new ResponseEntity<>("Join Success", HttpStatus.OK);
    }

//    @PostMapping("/join/teacher")
//    public ResponseEntity<?> joinMember(@Valid @RequestBody StudentJoinForm studentJoinForm) {
//
//    }
}
