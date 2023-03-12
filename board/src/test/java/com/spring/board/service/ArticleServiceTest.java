package com.spring.board.service;

import com.spring.board.domain.Article;
import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleDto;
import com.spring.board.dto.ArticleUpdateDto;
import com.spring.board.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.willReturn;

@DisplayName("비지니스 로직 테스트 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;
    @Mock
    private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다")
    @Test
    void givenSearchParameter_whenSearchingArticles_thenReturnsArticleList() {
        // given

        // when
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");

        // then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글 리스트를 반환한다")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // given

        // when
        ArticleDto article = sut.searchArticles(1L);

        // then
        assertThat(article).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다")
    @Test
    void givenArticleInfo_whenCreatingArticle_thenCreatesArticle() {
        // given
        ArticleDto dto = ArticleDto.of("title", "content", "#Test", LocalDateTime.now(), "Woo");
        // save 메서드가 호출되면 아무것도 하지 않도록 설정
        BDDMockito.given(articleRepository.save(any(Article.class))).willReturn(null);

        // when
        sut.saveArticle(dto);

        // then (save 메서드가 호출되었는지 확인)
        BDDMockito.then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID 와 수정정보를 입력하면 게시글을 수정한다")
    @Test
    void givenArticleIdAndInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // given
        BDDMockito.given(articleRepository.save(any(Article.class))).willReturn(null);

        // when
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#Test"));

        // then (save 메서드가 호출되었는지 확인)
        BDDMockito.then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID 를 입력하면 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // given
        BDDMockito.willDoNothing().given(articleRepository).delete(any(Article.class));

        // when
        sut.deleteArticle(1L);

        // then (save 메서드가 호출되었는지 확인)
        BDDMockito.then(articleRepository).should().delete(any(Article.class));
    }

}