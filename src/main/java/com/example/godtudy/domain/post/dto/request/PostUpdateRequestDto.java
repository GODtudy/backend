package com.example.godtudy.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequestDto {

    private String title;

    private String content;

    private String file;

    private String modifiedDate;

}
