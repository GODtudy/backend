package com.example.godtudy.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    PARENTS("PARENTS"),
    STUDENT("STUDENT");

    private final String key;
}
