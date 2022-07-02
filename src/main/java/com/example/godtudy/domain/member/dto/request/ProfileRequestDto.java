package com.example.godtudy.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    @Length(max=35)
    private String bio;

    private String profileImageUrl;
}
