package com.example.godtudy.domain.teacher.dto.response;

import com.example.godtudy.domain.member.entity.SubjectEnum;
import lombok.Data;

import java.util.List;

@Data
public class TeacherSearchResponseDto {

    private String name;
    private List<SubjectEnum> subjectEnums;
    private String profileImageUrl;
    private String bio;

}
