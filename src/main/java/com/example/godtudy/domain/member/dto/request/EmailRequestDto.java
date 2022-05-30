package com.example.godtudy.domain.member.dto.request;

import com.example.godtudy.global.validation.ValidationGroups;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EmailRequestDto {

    @NotBlank(message = "이메일은 필수 입력사항입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
            message = "올바르지 않은 이메일 형식입니다."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String email;
}
