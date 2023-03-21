package com.spring.board.repository;

import com.spring.board.config.JpaConfig;
import com.spring.board.domain.Article;
import com.spring.board.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


// 설정 안하면 실제 DB 사용, testdb profile 설정값 있으면 테스트 DB 사용하도록 설정
// 전역으로 설정하는 법 : application.yml에 test.database.replace=NONE 설정
@DisplayName("JPA 연결 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // test profile 설정값 없으면 실제 DB 사용
@ActiveProfiles("testdb")
//@Import(JpaConfig.class)   //  @EnableJpaAuditing 적용을 위해 import
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest    // tracsaction
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findById(1L).map(List::of).orElseGet(List::of);

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(1); // classpath:resources/data.sql 참조
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of(
                "newWoo", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#hashtag");

        // When
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // given
        UserAccount userAccount = userAccountRepository.save(
                UserAccount.of("Kim", "pwd", null, null, null));
        Article article = Article.of(userAccount, "article2", "content2", "#hashtag2");
        articleRepository.save(article);


        // when
        Article findArticle = articleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + 1L));
        System.out.println("findArticle = " + findArticle.toString());
        String hashTag = "#springBootTag";
        findArticle.updateHashTag(hashTag);

        // save 이후에 flush 해야 업데이트 쿼리 동작함 // articleRepository.flush();
        Article saveArticle = articleRepository.saveAndFlush(findArticle);

        // then
        assertThat(saveArticle.getHashtag()).isEqualTo("#springBootTag");
        assertThat(saveArticle).hasFieldOrPropertyWithValue("hashtag", hashTag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }

    @EnableJpaAuditing  // JPA Auditing 활성화, JpaConfig 를 치환한 코드
    @TestConfiguration  // 테스트용 설정 클래스 서비스 실행할때 Bean 등록 및 스캔 안된다
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("woo");
        }
    }
}