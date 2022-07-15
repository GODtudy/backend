package com.example.godtudy.domain.post.entity;

import com.example.godtudy.domain.BaseEntity;
import com.example.godtudy.domain.member.entity.Member;
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
    private String image;

    @Column(nullable = false)
    @ManyToOne
    private Member author;

    // == 연관관계 편의 메서드 == //

    public void setAuthor(Member author) {
        if (this.author != null) {
            this.author.getAdminPosts().remove(this);
        }
        this.author = author;
        author.getAdminPosts().add(this);
    }

    public static AdminPost createMemberNotice(Member member) {
        AdminPost adminPost = new AdminPost();
        adminPost.setAuthor(member);
        member.addNotice(adminPost);
        return adminPost;
    }

}
