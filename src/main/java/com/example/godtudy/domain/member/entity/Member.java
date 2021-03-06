package com.example.godtudy.domain.member.entity;

import com.example.godtudy.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthday;

    private Boolean emailVerified;

    private String emailCheckToken; // 이메일 인증 토큰

    private LocalDateTime emailCheckTokenGeneratedAt;

    private String profileImageUrl;

    private String bio;

    @OneToMany(mappedBy = "member")
    private List<Subject> subject = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public void setRole(Role role) {
        this.role = role;
    }

    // 이메일 체크 토근 랜덤한 값 생성
    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    // 이메일 인증 완료 시
    public void updateEmailVerified(boolean verified, LocalDateTime regDate){
        this.emailVerified = verified;
        this.createdDate = regDate;
    }
    //이메일 인증을 얼마나 자주 할 수 있을
    public boolean canSendConfirmEmail() {
//        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(1));
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
}
