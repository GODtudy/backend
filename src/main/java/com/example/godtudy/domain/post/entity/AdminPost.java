package com.example.godtudy.domain.post.entity;

import com.example.godtudy.domain.BaseEntity;
import com.example.godtudy.domain.comment.entity.Comment;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.member.entity.Subject;
import com.example.godtudy.domain.member.entity.SubjectEnum;
import com.example.godtudy.domain.post.dto.request.PostUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Slf4j
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPost extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminPostEnum noticeOrEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

//    @OneToMany(mappedBy = "adminPost", cascade = ALL, orphanRemoval = true)
//    private List<Comment> commentList = new ArrayList<>();
    //orphanRemoval 는 연관관계가 끊어진 자식 엔티티를 자동으로 삭제해주는 기능이다.

    // == 연관관계 편의 메서드 == //
    public void setAuthor(Member member) {
        if (this.member != null) {
            this.member.getAdminPosts().remove(this);
        }
        this.member = member;
        member.getAdminPosts().add(this);
    }

//    public void addComment(Comment comment) {
//        commentList.add(comment);
//    }


    public void setAdminPostEnum(String post){
        this.noticeOrEvent = AdminPostEnum.valueOf(post.toUpperCase());
    }

    public void updateAdminPost(PostUpdateRequestDto postUpdateRequestDto) {
        this.title = postUpdateRequestDto.getTitle();
        this.content = postUpdateRequestDto.getContent();
    }

    public void updateFile(String file) {
        this.file = file;
    }
}
