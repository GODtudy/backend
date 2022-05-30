package com.example.godtudy.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    PARENTS("ROLE_PARENTS"),
    STUDENT("ROLE_STUDENT"),
    TMP_TEACHER("ROLE_TMP_TEACHER"),
    TMP_PARENTS("ROLE_TMP_PARENTS"),
    TMP_STUDENT("ROLE_TMP_STUDENT");

    private final String key;
}
