package com.spring.board.service;

import com.spring.board.domain.Article;
import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.dto.ArticleDto;
import com.spring.board.repository.ArticleCommentRepository;
import com.spring.board.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비지니스 로직 테스트 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks
    private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;

    @DisplayName("게시글 ID로 검색하면, 댓글 리스트를 반환한다")
    @Test
    void givenArticleId_whenSearchingComment_thenReturnsCommentList() {
        // given
        Long articleId = 1L;

        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of("title", "content", "#hashTag")));
        // when
        List<ArticleCommentDto> comments = sut.searchArticleComments(articleId);

        // then
        assertThat(comments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

}