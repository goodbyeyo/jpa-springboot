package com.spring.board.dto;

import com.spring.board.domain.Article;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Article 과 ArticleComment 를 함께 조회할 때 사용하는 Dto
 */
public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        String hashtag,
        String title,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        Set<ArticleCommentDto> articleCommentDtos
        ) {
    public static ArticleWithCommentsDto of(Long id,
                                            UserAccountDto userAccountDto,
                                            String hashtag,
                                            String title,
                                            String content,
                                            LocalDateTime createdAt,
                                            String createdBy,
                                            LocalDateTime modifiedAt,
                                            String modifiedBy,
                                            Set<ArticleCommentDto> articleCommentDtos) {
        return new ArticleWithCommentsDto(id,
                userAccountDto,
                hashtag,
                title,
                content,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy,
                articleCommentDtos);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getHashtag(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getArticleComments().stream()
                        .map(ArticleCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)) // LinkedHashSet 으로 반환하여 순서 보장
        );
    }

}