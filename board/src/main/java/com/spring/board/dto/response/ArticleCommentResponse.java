package com.spring.board.dto.response;

import com.spring.board.domain.Article;
import com.spring.board.domain.ArticleComment;
import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.dto.UserAccountDto;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public record ArticleCommentResponse(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) implements Serializable {

    public static ArticleCommentDto of(Long id,
                                       Long articleId,
                                       UserAccountDto userAccountDto,
                                       String content,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        return new ArticleCommentResponse(
                dto.id(),
                dto.articleId(),
                UserAccountDto.from(Objects.requireNonNull(dto.userAccountDto().toEntity())),
                dto.content(),
                dto.createdAt(),
                dto.createdBy(),
                dto.modifiedAt(),
                dto.modifiedBy()
        );
    }

//    public static ArticleCommentResponse from(ArticleComment entity) {
//        return new ArticleCommentResponse(
//                entity.getId(),
//                entity.getArticle().getId(),
//                UserAccountDto.from(entity.getUserAccount()),
//                entity.getContent(),
//                entity.getCreatedAt(),
//                entity.getCreatedBy(),
//                entity.getModifiedAt(),
//                entity.getModifiedBy()
//        );
//    }



    public ArticleComment toEntity(Article entity) {
        return ArticleComment.of(
                entity,
                userAccountDto.toEntity(),
                content
        );
    }
}
