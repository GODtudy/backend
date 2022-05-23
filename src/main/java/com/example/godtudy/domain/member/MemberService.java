package com.example.godtudy.domain.member;

import com.example.godtudy.domain.member.dto.StudentJoinForm;
import com.example.godtudy.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member joinMember(StudentJoinForm studentJoinForm) {
        log.info(studentJoinForm.getPassword());
        studentJoinForm.setPassword(passwordEncoder.encode(studentJoinForm.getPassword())); //TODO passwordencoder를 dto에서 처리해서 넘겨줘도 되는건가? 여기서 이렇게 해줘도 되는건가
        log.info(studentJoinForm.getPassword());
        Member newMember = studentJoinForm.toEntity(); // TODO toentity를 여기서 불러와야하나??

        return memberRepository.save(newMember);
    }
}
