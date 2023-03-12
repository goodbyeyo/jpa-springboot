package com.spring.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(name = "idx_article_content", columnList = "content"),
        @Index(name = "idx_article_createdAt", columnList = "createdAt"),
        @Index(name = "idx_article_createdBy", columnList = "createdBy")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1 관계 설정 (댓글 N : 게시글 1)
    // @XToOne 관계는 모두 지연 로딩으로 설정해야 한다. 기본은 즉시 로딩
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Article article;            // 게시글

    @ManyToOne(optional = false)
    private UserAccount userAccount;    // 유저정보(ID, 닉네임, 이메일 등)

    @Column(nullable = false, length = 500)
    private String content;             // 본문

    @Builder
    private ArticleComment(UserAccount userAccount, Article article, String content) {
        this.userAccount = userAccount;
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return ArticleComment.builder()
                .userAccount(userAccount)
                .article(article)
                .content(content)
                .build();
    }

    // db entity 동등성 검사 (id 값이 같으면 동일한 entity) : lombok annotation 사용보다 퍼포먼스 증가
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        // 영속화 되지 않은 entity 동등성 검사 탈락
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
