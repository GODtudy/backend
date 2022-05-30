package com.example.godtudy.domain.member.dto.request;

import com.example.godtudy.global.validation.ValidationGroups;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class NicknameRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요", groups = ValidationGroups.NotEmptyGroup.class)
    @Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]{4,10}",
            message = "닉네임은 한글, 영어, 숫자만 4 ~10자리로 입력 가능합니다"
            , groups = ValidationGroups.PatternCheckGroup.class)
    private String nickname;

}
