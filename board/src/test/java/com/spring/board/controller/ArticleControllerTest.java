package com.spring.board.controller;

import com.spring.board.config.SecurityConfig;
import com.spring.board.dto.ArticleWithCommentsDto;
import com.spring.board.dto.UserAccountDto;
import com.spring.board.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View Controller Test")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean // Type 과 Filed 에만 선언 가 -> 필드주입
    private ArticleService articleService;

    // @WebMvcTest, SpringExtension.class -> @Autowired 사용 가능, @MockBean 사용불가
    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view] [GET] 게시글 리스트 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // given    필드중 일부만 matcher 할 수 없으므로 eq, any 사용
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class)))
                .willReturn(Page.empty());

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
    }

//    @DisplayName("[view] [GET] 게시글 상세 페이지 - 정상 호출 ")
//    @Test
//    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
//        // given
//        Long articleId = 1L;
//        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentDto());
//
//        // when & then
//        mvc.perform(get("/articles/" + articleId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("articles/detail"))
//                .andExpect(model().attributeExists("article"))
//                .andExpect(model().attributeExists("articleComments"));
//        then(articleService).should().getArticle(articleId);
//    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출 ")
    @Test
    public void articleSearch() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/search"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 해시태그 검색 페이지 - 정상 호출 ")
    @Test
    public void article() throws Exception {
        // given

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    private ArticleWithCommentsDto createArticleWithCommentDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "woo",
                LocalDateTime.now(),
                "woo",
                Set.of()
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "woo",
                "password",
                "woo@gmai.com",
                "woo",
                "memo",
                LocalDateTime.now(),
                "woo",
                LocalDateTime.now(),
                "woo"
        );
    }
}