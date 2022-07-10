package com.example.godtudy.domain.study.entity;

import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.study.dto.request.StudyDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Builder
@Entity
public class Study {

    @GeneratedValue @Id
    @Column(name = "study_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Member teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Member student;

    private String name;

    private String url;

    private String subject;

    private String shortDescription;

    public void updateStudy(StudyDto studyDto) {
        this.name = studyDto.getName();
        this.url = studyDto.getUrl();
        this.subject = studyDto.getSubject();
        this.shortDescription = studyDto.getShortDescription();
    }

    public void updateTeacher(Member teacher) {
        this.teacher = teacher;
    }
}
