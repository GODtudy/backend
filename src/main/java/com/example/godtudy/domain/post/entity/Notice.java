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
public class Notice extends BaseEntity {

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
            this.author.getNotices().remove(this);
        }
        this.author = author;
        author.getNotices().add(this);
    }

    public static Notice createMemberNotice(Member member) {
        Notice notice = new Notice();
        notice.setAuthor(member);
        member.addNotice(notice);
        return notice;
    }

}
