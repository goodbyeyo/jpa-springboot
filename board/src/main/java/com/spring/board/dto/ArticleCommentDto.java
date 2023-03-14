package com.spring.board.dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.spring.board.domain.ArticleComment} entity
 */
public record ArticleCommentDto(
        Long id,
        Long articleId,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleCommentDto of(Long id,
                                       Long articleId,
                                       String content,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy) {
        return new ArticleCommentDto(id, articleId, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}