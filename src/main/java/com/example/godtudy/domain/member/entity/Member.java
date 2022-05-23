package com.example.godtudy.domain.member.entity;

import com.example.godtudy.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private Date birthday;

    private Boolean emailVerified;

    private String profileImageUrl;

    private String bio;

    private String subject;

    @Enumerated(EnumType.STRING)
    private Role role;


}
