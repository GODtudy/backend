package com.example.godtudy.domain.member.dto.response.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindPasswordResponseDto {

    private String tmpPassword;
}
