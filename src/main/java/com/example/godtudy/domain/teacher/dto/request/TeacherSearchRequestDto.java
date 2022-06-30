package com.example.godtudy.domain.teacher.dto.request;

import com.example.godtudy.domain.member.entity.SubjectEnum;
import lombok.Data;

@Data
public class TeacherSearchRequestDto {

    private String type;
    private String name;
    private SubjectEnum subject;
}
