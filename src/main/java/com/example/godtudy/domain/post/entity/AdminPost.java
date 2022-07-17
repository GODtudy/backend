package com.example.godtudy.domain.post.entity;

import com.example.godtudy.domain.BaseEntity;
import com.example.godtudy.domain.member.entity.Member;
import com.example.godtudy.domain.post.dto.request.PostSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private AdminPostEnum noticeOrEvent;

    public void setAdminPostEnum(String post){
        this.noticeOrEvent = AdminPostEnum.valueOf(post.toUpperCase());
    }

    // == 연관관계 편의 메서드 == //

    public void setAuthor(Member author) {
        this.member = author;
        author.getAdminPosts().add(this);
    }

    public void updateAdminPost(PostSaveRequestDto postSaveRequestDto) {
        this.title = postSaveRequestDto.getTitle();
        this.content = postSaveRequestDto.getContent();
        this.file = postSaveRequestDto.getFile();
        this.modifiedDate = postSaveRequestDto.getModifiedDate();
    }
}
