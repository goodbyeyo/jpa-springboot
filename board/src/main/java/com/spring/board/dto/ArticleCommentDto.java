package com.spring.board.dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.spring.board.domain.ArticleComment} entity
 */
public record ArticleCommentDto(
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleCommentDto of(String content,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy) {
        return new ArticleCommentDto(content, createdAt, createdBy, modifiedAt, modifiedBy);
    }
}