package com.spring.board.controller;


import com.spring.board.dto.UserAccountDto;
import com.spring.board.dto.request.ArticleCommentRequest;
import com.spring.board.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
        // TODO : 인증정보를 넣어줘야 한다
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "woo", "woo1234", "woo@gmail.com", "woo", "memo")));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId,Long articleId) {
        articleCommentService.deleteArticleComment(commentId);
        return "redirect:/articles/" + articleId;
    }
}
