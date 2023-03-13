package com.spring.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
// @ToString   // lazy load 필드 때문에 성능 저하 또는 메모리 이슈 일으킬수 있음, 순환참조 문제 발생
@ToString(callSuper = true)    // 부모 클래스의 필드까지 출력 (AuditingFields)
@Table(indexes = {
        @Index(name = "idx_article_title", columnList = "title"),
        @Index(name = "idx_article_hashtag", columnList = "hashtag"),
        @Index(name = "idx_article_createdAt", columnList = "createdAt"),
        @Index(name = "idx_article_createdBy", columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class) // Spring Data JPA Auditing 활성화 (AuditingFields 에서 상속받아서 사용)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // Entity : 기본생성자 필요, PROTECTED <- 생성자 접근 불가
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(optional = false)
    private UserAccount userAccount;    // 유저정보(ID, 닉네임, 이메일 등)

    @Column(nullable = false)
    private String title;                // 제목

    @Column(nullable = false, length = 10000)
    private String content;             // 본문

    private String hashtag;              // 해시태그

    // 1:N 관계 설정 (게시글 1 : 댓글 N) List, Map, Set(중복 방지) 등을 활용 가능
    // 양방향 바인딩 : articleComments 필드에 article 필드를 추가
    // 실무에서는 양방향 바인딩을 잘 사용하지 않는다, cascade 에 의해 강하게 결합되어 있기때문에
    // 1) 데이터를 마이그레이션하거나 수정하기 어렵고 2) 원하지 않는 데이터 소실 발생할수 있음
    // 게시글을 삭제하면 연관되어있는 댓글도 삭제되도록 설정됨
    @ToString.Exclude
    // @OrderBy("id ASC")  // 댓글을 id 순으로 정렬
    @OrderBy("createdAt DESC") // 댓글을 생성일자 역순으로 정렬 (최신순)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @Builder
    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return Article.builder()
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }

    public void updateHashTag(String hashtag) {
        this.hashtag = hashtag;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override   // equals, hashCode : id 값으로 비교
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        // DB 에 데이터를 저장하지 않았을때 Entity -> null 이므로
        // 영속화 되지 않은 entity 동등성 검사 탈락시키도록 null check
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
