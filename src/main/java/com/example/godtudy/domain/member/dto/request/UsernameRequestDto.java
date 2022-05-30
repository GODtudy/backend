package com.example.godtudy.domain.member.dto.request;

import com.example.godtudy.global.validation.ValidationGroups;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UsernameRequestDto {

    @NotBlank(message = "아이디는 필수 입력사항입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "[a-zA-Z0-9]{2,15}",
            message = "아이디는 영문, 숫자 조합만 가능하며 2 ~ 15 자리 까지 가능합니다."
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String username;

}
