package com.spring.board.repository;

import com.spring.board.config.JpaConfig;
import com.spring.board.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// 설정 안하면 실제 DB 사용, testdb profile 설정값 있으면 테스트 DB 사용하도록 설정
// 전역으로 설정하는 법 : application.yml에 test.database.replace=NONE 설정
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // test profile 설정값 없으면 실제 DB 사용
@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)   //  @EnableJpaAuditing 적용을 위해 import
@DataJpaTest    // tracsaction
class ArticleRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public ArticleRepositoryTest(@Autowired ArticleRepository articleRepository,
                                 @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void selectTest() {
        // given
        Article article = Article.of("게시판1", "게시판 내용을 등록", "#블로깅");

        // when
        articleRepository.save(article);
        List<Article> articleList = articleRepository.findAll();

        // then
        assertThat(articleList).isNotNull().hasSize(1);
        assertEquals(articleList.size(), 1);
        assertThat(articleList.get(0).getTitle()).isEqualTo("게시판1");
        assertThat(articleList.get(0).getContent()).isEqualTo("게시판 내용을 등록");
        assertThat(articleList.get(0).getHashtag()).isEqualTo("#블로깅");

    }

    @DisplayName("insert test")
    @Test
    void insertTest() {
        // given
        long previousCount = articleRepository.count();
        Article article = Article.of("article1", "content1", "#hashtag1");

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);

    }

    @DisplayName("update test")
    @Test
    void updateTest() {
        // given
        Article article = Article.of("article2", "content2", "#hashtag2");
        articleRepository.save(article);

        // when
        Article findArticle = articleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + 1L));
        String hashTag = "#springBootTag";
        findArticle.updateHashTag(hashTag);

        // Article saveArticle2 = articleRepository.save(findArticle);
        // save 이후에 flush 해야 업데이트 쿼리 동작함
        // articleRepository.flush();
        Article saveArticle = articleRepository.saveAndFlush(findArticle);


        // save 이후에 flush 해야 업데이트 쿼리 동작함
        articleRepository.flush();

        // then
        assertThat(saveArticle.getHashtag()).isEqualTo("#springBootTag");
        assertThat(saveArticle).hasFieldOrPropertyWithValue("hashtag", hashTag);
    }

    @DisplayName("delete test")
    @Test
    void deleteTest() {
        // given
        Article article = Article.of("article3", "content3", "#hashtag3");
        Article article2 = Article.of("article4", "content4", "#hashtag4");

        articleRepository.saveAllAndFlush(List.of(article, article2));

        // when
        articleRepository.deleteById(4L);

        // then
        assertThat(articleRepository.count()).isEqualTo(1);
    }

}