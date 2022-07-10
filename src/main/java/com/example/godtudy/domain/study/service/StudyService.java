package com.example.godtudy.domain.study.service;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Role;
import com.example.godtudy.domain.study.dto.request.StudyDto;
import com.example.godtudy.domain.study.dto.response.CreateStudyResponseDto;
import com.example.godtudy.domain.study.entity.Study;
import com.example.godtudy.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    /**
     * 중복 확인
     */
    public boolean isStudyNameDuplicate(String url){

        Study study = studyRepository.findByUrl(url);
        if (study!=null) {
            return false;
        }
        return true;
    }

    /**
     * 공부방 생성
     */
    public ResponseEntity<?> createStudy(Member member, StudyDto studyDto) {
        Study newStudy = studyDto.toEntity();
        if (member.getRole() == Role.TEACHER || member.getRole() == Role.ADMIN) {
            studyRepository.save(newStudy);
            newStudy.updateTeacher(member);
            CreateStudyResponseDto response = CreateStudyResponseDto.builder()
                    .url(newStudy.getUrl())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("study를 찾을 수 없습니다", HttpStatus.BAD_REQUEST);
    }
}
