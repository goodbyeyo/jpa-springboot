//package com.spring.board.service;
//
//import com.spring.board.domain.Article;
//import com.spring.board.domain.ArticleComment;
//import com.spring.board.domain.UserAccount;
//import com.spring.board.dto.ArticleCommentDto;
//import com.spring.board.dto.UserAccountDto;
//import com.spring.board.repository.ArticleCommentRepository;
//import com.spring.board.repository.ArticleRepository;
//import com.spring.board.repository.UserAccountRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.*;
//
//@DisplayName("비지니스 로직 테스트 - 댓글")
//@ExtendWith(MockitoExtension.class)
//class ArticleCommentServiceTest {
//
//    @InjectMocks
//    private ArticleCommentService sut;
//
//    @Mock private ArticleRepository articleRepository;
//    @Mock private ArticleCommentRepository articleCommentRepository;
//    @Mock private UserAccountRepository userAccountRepository;
//
//    @DisplayName("게시글 ID로 검색하면, 댓글 리스트를 반환한다")
//    @Test
//    void givenArticleId_whenSearchingComment_thenReturnsCommentList() {
//        // given
//        Long articleId = 1L;
//        ArticleComment expected = createArticleComment("content");
//        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
//
//        // when
//        List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);
//
//        // then
//        assertThat(actual)
//                .hasSize(1)
//                        .first().hasFieldOrPropertyWithValue("content", expected.getContent());
//        then(articleRepository).should().findById(articleId);
//    }
//
//    @DisplayName("댓글 정보를 입력하면 댓글을 저장한다")
//    @Test
//    void givenCommentInfo_whenSavingComment_thenSaveComment() {
//        // given
//        ArticleCommentDto dto = createArticleCommentDto("댓글");
//        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
//        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
//
//        // when
//        sut.saveArticleComment(dto);
//
//        // then
//        then(articleCommentRepository).should().save(any(ArticleComment.class));
//    }
//
//    @DisplayName("게시글 저장을 시도했는데 맞는 게시글이 없으면 경고 로그를 찍고 아무것도 하지 않는다")
//    @Test
//    void givenSavingComment_whenNotMatchArticle_thenWarningLogAndDoNothing() {
//        // given
//        ArticleCommentDto dto = createArticleCommentDto("댓글");
//        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);
//
//        // when
//        sut.saveArticleComment(dto);
//
//        // then
//        then(articleRepository).should().getReferenceById(dto.articleId());
//        then(articleCommentRepository).shouldHaveNoInteractions();
//    }
//
//    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다")
//    @Test
//    void givenCommentInfo_whenUpdatingComment_thenUpdateComment() {
//        // given
//        String oldContent = "content";
//        String updatedContent = "댓글";
//        ArticleComment articleComment = createArticleComment(oldContent);
//        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
//        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);
//
//        // when
//        sut.updateArticleComment(dto);
//
//        // then
//        assertThat(articleComment.getContent())
//                .isNotEqualTo(oldContent)
//                .isEqualTo(updatedContent);
//        then(articleCommentRepository).should().getReferenceById(dto.id());
//    }
//
//    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무것도 안한다")
//    @Test
//    void givenCommentInfo_whenUpdatingComment_thenUpdateComment() {
//
//        ArticleCommentDto dto = createArticleComment("댓글");
//        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
//
//        // when
//        sut.updateArticleComment(dto);
//
//        // then
//        then(articleCommentRepository).should().getReferenceById(dto.id());
//    }
//
//    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다")
//    @Test
//    void givenCommentId_whenDeletingComment_thenDeleteComment() {
//        // given
//        Long articleCommentId = 1L;
//        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);
//
//        // when
//        sut.deleteArticleComment(articleCommentId);
//
//        // then
//        then(articleCommentRepository).should().deleteById(articleCommentId);
//
//    }
//
//    private ArticleCommentDto createArticleCommentDto(String content) {
//        return ArticleCommentDto.of(
//                1L,
//                1L,
//                createUserAccountDto(),
//                content,
//                LocalDateTime.now(),
//                "woo",
//                LocalDateTime.now(),
//                "woo"
//        );
//    }
//
//    private UserAccountDto createUserAccountDto() {
//        return UserAccountDto.of(
//                1L,
//                "Woo",
//                "password",
//                "Woo@gmail.com",
//                "Woo",
//                "This is memo",
//                LocalDateTime.now(),
//                "woo",
//                LocalDateTime.now(),
//                "woo"
//        );
//    }
//
//    private ArticleComment createArticleComment(String content) {
//        return ArticleComment.of(
//                Article.of(createUserAccount(), "title", "content", "hashtag"),
//                createUserAccount(),
//                content
//        );
//    }
//
//    private UserAccount createUserAccount() {
//        return UserAccount.of(
//                "Woo",
//                "password",
//                "woo@gmail.com",
//                "Woo",
//                null
//        );
//    }
//
//    private Article createArticle() {
//        return Article.of(
//                createUserAccount(),
//                "title",
//                "content",
//                "hashtag"
//        );
//    }
//
//}