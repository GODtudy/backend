package com.example.godtudy.domain.study.dto.request;

import com.example.godtudy.domain.study.entity.Study;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class StudyDto {

    private String name;

    @NotEmpty
    @Length(min=2, max = 20)
    private String url;

    private String subject;

    private String shortDescription;

    public Study toEntity(){
        return Study.builder()
                .name(name)
                .url(url)
                .subject(subject)
                .shortDescription(shortDescription)
                .build();
    }
}
