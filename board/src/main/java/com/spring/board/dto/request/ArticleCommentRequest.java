package com.spring.board.dto.request;

import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.dto.UserAccountDto;

/**
 * A DTO for the {@link com.spring.board.domain.ArticleComment} entity
 */
public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(articleId, userAccountDto, content);
    }
}